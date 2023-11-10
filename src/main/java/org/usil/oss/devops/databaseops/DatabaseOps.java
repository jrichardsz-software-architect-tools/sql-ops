package org.usil.oss.devops.databaseops;

import com.mysql.cj.util.StringUtils;
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

public class DatabaseOps implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private static final Logger logger = LoggerFactory.getLogger(DatabaseOps.class);
  
  private DatabaseExecutor databaseHelper = new DatabaseExecutor();
  
  public HashMap<String, Object> perform(String databaseHost, int databasePort, String databaseName, String databaseUser, String databasePassword, String databaseDbaUser, String databaseDbaPassword, String scriptsFolder, String engine, boolean verboseLog) throws Exception {
    if (verboseLog) {
      logger.info("Setting debug level");
      LoggerHelper.setDebugLevel();
    } 
    if (StringUtils.isEmptyOrWhitespaceOnly(databaseHost))
      throw new Exception("database host is required"); 
    if (StringUtils.isEmptyOrWhitespaceOnly(databaseName))
      throw new Exception("database name is required"); 
    if (StringUtils.isEmptyOrWhitespaceOnly(databaseUser))
      throw new Exception("database user is required"); 
    if (StringUtils.isEmptyOrWhitespaceOnly(databasePassword))
      throw new Exception("database password is required"); 
    if (StringUtils.isEmptyOrWhitespaceOnly(scriptsFolder))
      throw new Exception("script folder is required"); 
    if (StringUtils.isEmptyOrWhitespaceOnly(engine))
      throw new Exception("database engine is required"); 
    logger.info("Starting database operations...");
    ArrayList<String> queries = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".sql$");
    if (queries.isEmpty())
      throw new Exception("folder don't contains any .sql file: " + scriptsFolder); 
    logger.info("scripts");
    logger.info(queries.toString());
    ArrayList<String> rollbacks = FileHelper.readFilesAtRoot(new File(scriptsFolder), ".rollback$");
    logger.info("rollbacks");
    logger.info(rollbacks.toString());
    FileHelper.detectRequiredPairs(queries, rollbacks, ".rollback");
    String sqlShowErrors = null;
    ArrayList<?> beforeErrors = new ArrayList();
    ArrayList<ArrayList<?>> successOutputs = new ArrayList<>();
    ArrayList<String> errorOutputs = new ArrayList<>();
    ArrayList<?> afterErrors = new ArrayList();
    if (ClassPathProperties.hasProperty(engine + ".errorQueryFile"))
      if (databaseDbaUser != null && !databaseDbaPassword.isEmpty()) {
        sqlShowErrors = FileHelper.getFileAsStringFromClasspath(
            ClassPathProperties.getProperty(engine + ".errorQueryFile"));
        beforeErrors = this.databaseHelper.executeSimpleScriptString(engine, databaseHost, databasePort, databaseName, databaseUser, databasePassword, sqlShowErrors);
        logger.info("Database errors before the scripts execution: " + beforeErrors.size());
        logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
      } else {
        logger.info("Super user was not enabled, so the error check before execution is not possible");
      }  
    HashMap<String, Object> executionDetails = new HashMap<>();
    ArrayList<String> executedQueries = new ArrayList<>();
    ArrayList<String> executedRollbacks = new ArrayList<>();
    ArrayList<String> rollbackSuccessOutputs = new ArrayList<>();
    ArrayList<String> rollbackErrorOutputs = new ArrayList<>();
    String currentScript = null;
    String currentScriptSingleName = null;
    try {
      for (String scriptPath : queries) {
        currentScript = scriptPath;
        currentScriptSingleName = currentScript.replaceAll(scriptsFolder, "");
        ArrayList<?> scriptOutput = this.databaseHelper.executeSimpleScriptFile(engine, databaseHost, databasePort, databaseName, databaseUser, databasePassword, databaseDbaUser, databaseDbaPassword, currentScript);
        logger.info(String.format("script: %s , status: success", new Object[] { currentScriptSingleName }));
        executedQueries.add(currentScript);
        successOutputs.add(scriptOutput);
      } 
      executionDetails.put("status", "success");
      if (ClassPathProperties.hasProperty(engine + ".errorQueryFile"))
        if (databaseDbaUser != null && !databaseDbaPassword.isEmpty()) {
          afterErrors = this.databaseHelper.executeSimpleScriptString(engine, databaseHost, databasePort, databaseName, databaseDbaUser, databaseDbaPassword, sqlShowErrors);
          if (afterErrors.size() > beforeErrors.size()) {
            logger.info("scripts could caused new errors.");
            logger.info(TableAscciHelper.createSimpleTable(beforeErrors));
          } 
        } else {
          logger.info("Super user was not enabled, so the error check after execution is not possible");
        }  
    } catch (Exception e) {
      executionDetails.put("status", "error");
      String errorMessage = String.format("script: %s , status: error", new Object[] { currentScriptSingleName });
      errorOutputs.add(currentScriptSingleName + " : " + e.getMessage());
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
            scriptOutput = this.databaseHelper.executeSimpleScriptFile(engine, databaseHost, databasePort, databaseName, databaseUser, databasePassword, databaseDbaUser, databaseDbaPassword, rollbackFileLocation);
            rollbackSuccessOutputs.add(singleRollbackScriptName + " : " + scriptOutput);
            executedRollbacks.add(executedScript + ".rollback");
            logger.error(String.format("rollback: %s , status: success", new Object[] { singleRollbackScriptName }));
          } catch (Exception ex) {
            logger.error("If a rollback fails, God, what more do you want from me?");
            logger.error(String.format("rollback: %s , status: error", new Object[] { singleRollbackScriptName }));
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
    String databaseDbaUser = commandLine.getOptionValue("database_dba_user");
    String databaseDbaPassword = commandLine.getOptionValue("database_dba_password");
    String scriptsFolder = commandLine.getOptionValue("scripts_folder");
    String engine = commandLine.getOptionValue("engine");
    boolean verboseLog = commandLine.hasOption("verbose_log");
    return perform(databaseHost, databasePort, databaseName, databaseUser, databasePassword, databaseDbaUser, databaseDbaPassword, scriptsFolder, engine, verboseLog);
  }
}
