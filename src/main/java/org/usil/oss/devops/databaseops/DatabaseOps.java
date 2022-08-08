package org.usil.oss.devops.databaseops;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usil.oss.common.ascii.TableAscciHelper;
import org.usil.oss.common.cli.ArgumentsHelper;
import org.usil.oss.common.database.DatabaseExecutor;
import org.usil.oss.common.exception.ExceptionHelper;
import org.usil.oss.common.file.ClassPathProperties;
import org.usil.oss.common.file.FileHelper;
import org.usil.oss.common.logger.LoggerHelper;
import com.mysql.cj.util.StringUtils;

public class DatabaseOps implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(DatabaseOps.class);

  private DatabaseExecutor databaseHelper = new DatabaseExecutor();

  public HashMap<String, Object> perform(String databaseHost, int databasePort, String databaseName,
      String databaseUser, String databasePassword, String scriptsFolder, String engine,
      boolean verboseLog) throws Exception {

    if (verboseLog) {
      LoggerHelper.setDebugLevel();
    }

    if (StringUtils.isEmptyOrWhitespaceOnly(databaseHost)) {
      throw new Exception("database host is required");
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(databaseName)) {
      throw new Exception("database name is required");
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(databaseUser)) {
      throw new Exception("database user is required");
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(databasePassword)) {
      throw new Exception("database password is required");
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(scriptsFolder)) {
      throw new Exception("script folder is required");
    }
    if (StringUtils.isEmptyOrWhitespaceOnly(engine)) {
      throw new Exception("database engine is required");
    }

    logger.info("Starting database operations...");

    ArrayList<String> queries = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".sql$");

    if (queries.isEmpty()) {
      throw new Exception("folder don't contains any .sql file: " + scriptsFolder);
    }

    logger.info("scripts");
    logger.info(queries.toString());

    ArrayList<String> rollbacks = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".rollback$");
    logger.info("rollbacks");
    logger.info(rollbacks.toString());

    FileHelper.detectRequiredPairs(queries, rollbacks, ".rollback");

    String sqlShowErrors = null;
    ArrayList<?> beforeErrors = new ArrayList<>();

    ArrayList<ArrayList<?>> successOutputs = new ArrayList<>();
    ArrayList<String> errorOutputs = new ArrayList<>();
    ArrayList<?> afterErrors = new ArrayList<>();

    if (ClassPathProperties.hasProperty(engine + ".errorQueryFile")) {
      sqlShowErrors = FileHelper.getFileAsStringFromClasspath(
          ClassPathProperties.getProperty(engine + ".errorQueryFile"));

      beforeErrors = databaseHelper.executeSimpleScriptString(engine, databaseHost, databasePort,
          databaseName, databaseUser, databasePassword, sqlShowErrors);

      logger.info("Database errors before the scripts execution: " + beforeErrors.size());
      logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
    }

    HashMap<String, Object> executionDetails = new HashMap<String, Object>();

    ArrayList<String> executedQueries = new ArrayList<String>();
    ArrayList<String> executedRollbacks = new ArrayList<String>();
    ArrayList<String> rollbackSuccessOutputs = new ArrayList<String>();
    ArrayList<String> rollbackErrorOutputs = new ArrayList<String>();
    String currentScript = null;
    String currentScriptSingleName = null;
    try {
      for (String scriptPath : queries) {
        currentScript = scriptPath;
        currentScriptSingleName = currentScript.replaceAll(scriptsFolder, "");
        logger.debug(String.format("script: %s , status: starting", currentScriptSingleName));
        ArrayList<?> scriptOutput = databaseHelper.executeSimpleScriptFile(engine, databaseHost,
            databasePort, databaseName, databaseUser, databasePassword, currentScript);
        logger.info(String.format("script: %s , status: success", currentScriptSingleName));
        executedQueries.add(currentScript);
        successOutputs.add(scriptOutput);
      }
      executionDetails.put("status", "success");
      if (ClassPathProperties.hasProperty(engine + ".errorQueryFile")) {
        // detect if errors increased
        afterErrors = databaseHelper.executeSimpleScriptString(engine, databaseHost, databasePort,
            databaseName, databaseUser, databasePassword, sqlShowErrors);
        if (afterErrors.size() > beforeErrors.size()) {
          logger.info("scripts could caused new errors.");
          logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
        }
      }
    } catch (Exception e) {
      executionDetails.put("status", "error");
      String errorMessage = String.format("script: %s , status: error", currentScriptSingleName);
      errorOutputs.add(currentScriptSingleName + " : " + e.getCause());
      if (verboseLog) {
        logger.error(errorMessage, e);
      } else {
        logger.error(errorMessage);
        logger.error(ExceptionHelper.summarizeTrace(e, true));
      }

      logger.info("Rollback scripts will be executed");
      if (executedQueries.size() < 1) {
        logger.info("Rollback is not required because error was throwed in first script");
      } else {
        Collections.reverse(executedQueries);

        for (String executedScript : executedQueries) {
          ArrayList<?> scriptOutput = null;
          String rollbackFileLocation = executedScript + ".rollback";
          String singleRollbackScriptName = rollbackFileLocation.replaceAll(scriptsFolder, "");
          try {
            scriptOutput = databaseHelper.executeSimpleScriptFile(engine, databaseHost,
                databasePort, databaseName, databaseUser, databasePassword, rollbackFileLocation);
            rollbackSuccessOutputs.add(singleRollbackScriptName + " : " + scriptOutput);
            executedRollbacks.add(executedScript + ".rollback");
            logger.error(String.format("rollback: %s , status: success", singleRollbackScriptName));
          } catch (Exception ex) {
            logger.error("If a rollback fails, God, what more do you want from me?");
            logger.error(String.format("rollback: %s , status: error", singleRollbackScriptName));
            rollbackErrorOutputs.add(singleRollbackScriptName + " : " + ex.getCause());
            logger.error("By default on rollback error, the entire execution ends.");
            break;
          }

        }
      }
    }

    logger.info("By JRichardsz");


    executionDetails.put("afterErrors", afterErrors);
    executionDetails.put("beforeErrors", beforeErrors);
    executionDetails.put("queryScripts", queries);
    executionDetails.put("rollbackScripts", rollbacks);
    executionDetails.put("executedQueryScripts", executedQueries);
    executionDetails.put("executedRollbackScripts", executedRollbacks);
    executionDetails.put("successOutputs", successOutputs);
    executionDetails.put("errorOutputs", errorOutputs);
    executionDetails.put("rollbackErrorOutputs", rollbackSuccessOutputs);
    executionDetails.put("rollbackSuccessOutputs", rollbackSuccessOutputs);
    return executionDetails;
  }

  public HashMap<String, Object> perform(String[] args) throws Exception {
    ArgumentsHelper argumentsHelper = new ArgumentsHelper();
    CommandLine commandLine = argumentsHelper.getArguments(args);

    String databaseHost = commandLine.getOptionValue("database_host");
    int databasePort = Integer.parseInt(commandLine.getOptionValue("database_port"));
    String databaseName = commandLine.getOptionValue("database_name");
    String databaseUser = commandLine.getOptionValue("database_user");
    String databasePassword = commandLine.getOptionValue("database_password");

    String scriptsFolder = commandLine.getOptionValue("scripts_folder");
    String engine = commandLine.getOptionValue("engine");
    boolean verboseLog = commandLine.hasOption("verbose_log");
    return perform(databaseHost, databasePort, databaseName, databaseUser, databasePassword,
        scriptsFolder, engine, verboseLog);
  }
}
