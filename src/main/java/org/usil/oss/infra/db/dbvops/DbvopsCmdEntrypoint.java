package org.usil.oss.infra.db.dbvops;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.usil.oss.common.ascii.TableAscciHelper;
import org.usil.oss.common.database.DatabaseHelper;
import org.usil.oss.common.exception.ExceptionHelper;
import org.usil.oss.common.file.ClassPathProperties;
import org.usil.oss.common.file.FileHelper;
import org.usil.oss.common.logger.LoggerHelper;
import org.usil.oss.common.string.StringHelper;

public class DbvopsCmdEntrypoint {

  private final static Logger logger = LogManager.getLogger(DbvopsCmdEntrypoint.class);

  public static void main(String[] args) throws Exception {
    ArgumentsHelper argumentsHelper = new ArgumentsHelper();
    CommandLine commandLine = argumentsHelper.getArguments(args);

    HashMap<String, String> oracleParams =
        getOracleParameters(commandLine.getOptionValue("dbvops_env_params_id"), commandLine);

    String host = oracleParams.get("database_host");
    int port = Integer.parseInt(oracleParams.get("database_port"));
    String name = oracleParams.get("database_name");
    String user = oracleParams.get("database_user");
    String password = oracleParams.get("database_password");

    String scriptsFolder = commandLine.getOptionValue("scripts_folder");
    String engine = commandLine.getOptionValue("engine");

    if (commandLine.hasOption("verbose_log")) {
      LoggerHelper.setDebugLevel();
    }

    ArrayList<String> scripts = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".sql$");
    logger.info("scripts");
    logger.info(scripts);

    ArrayList<String> rollback = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".rollback$");
    logger.info("rollbacks");
    logger.info(rollback);

    FileHelper.detectRequiredPairs(scripts, rollback, ".rollback");

    DatabaseHelper databaseHelper = new DatabaseHelper();

    String sqlShowErrors = null;
    ArrayList<?> beforeErrors = null;

    if (ClassPathProperties.hasProperty(engine + ".showErrorQueryFile")) {
      sqlShowErrors = FileHelper.getFileAsStringFromClasspath(
          ClassPathProperties.getProperty(engine + ".showErrorQueryFile"));
      beforeErrors = databaseHelper.executeSimpleScriptString(engine, host, port, name, user,
          password, sqlShowErrors);

      logger.info("Database errors before the scripts execution: " + beforeErrors.size());
      logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
    }

    ArrayList<String> executedScripts = new ArrayList<String>();
    String currentScript = null;
    try {
      for (String scriptPath : scripts) {
        currentScript = scriptPath;
        databaseHelper.executeSimpleScriptFile(engine, host, port, name, user, password,
            currentScript);
        logger.info(String.format("script: %s , status: success",
            currentScript.replace(scriptsFolder, "")));
        executedScripts.add(currentScript);

        if (ClassPathProperties.hasProperty(engine + ".showErrorQueryFile")) {
          // detect if errors increased
          ArrayList<?> thisErrors = databaseHelper.executeSimpleScriptString(engine, host, port,
              name, user, password, sqlShowErrors);
          if (thisErrors.size() > beforeErrors.size()) {
            logger.info("Last script caused new errors.");
            logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
          }
        }

      }
    } catch (Exception e) {
      String errorMessage =
          String.format("script: %s , status: error", currentScript.replace(scriptsFolder, ""));
      if (logger.isDebugEnabled()) {
        logger.error(errorMessage, e);
      } else {
        logger.error(errorMessage);
        logger.error(ExceptionHelper.summarizeTrace(e, true));
      }

      logger.info("Rollback scrtips will be executed");
      if (executedScripts.size() <= 1) {
        logger.info("Rollback is not required because error was throwed in first script");
        System.exit(1);
      }

      Collections.reverse(executedScripts);

      for (String executedScript : executedScripts) {
        databaseHelper.executeSimpleScriptFile(engine, host, port, name, user, password,
            executedScript + ".rollback");
        logger.info(String.format("rollback: %s , status: success",
            currentScript.replace(scriptsFolder, "")));
      }

    }

    logger.info("By JRichardsz");
    System.exit(0);
  }

  private static HashMap<String, String> getOracleParameters(String oracleParamsId,
      CommandLine commandLine) {

    HashMap<String, String> params = new HashMap<String, String>();
    if (oracleParamsId != null && !oracleParamsId.isEmpty()) {
      String rawParameters = System.getenv(oracleParamsId);
      return StringHelper.severalKeyValuesInlineToMap(rawParameters);
    } else {
      params.put("database_host", commandLine.getOptionValue("database_host"));
      params.put("database_port", commandLine.getOptionValue("database_port"));
      params.put("database_name", commandLine.getOptionValue("database_name"));
      params.put("database_user", commandLine.getOptionValue("database_user"));
      params.put("database_password", commandLine.getOptionValue("database_password"));
    }

    return params;
  }
}
