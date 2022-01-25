package org.usil.oss.common.model;

import java.util.ArrayList;

public class ExecutionMetadata {

  private ArrayList<String> queryScripts;
  private ArrayList<String> rollbackScripts;
  private ArrayList<?> beforeErrors;
  private ArrayList<?> afterErrors;
  private ArrayList<ArrayList<?>> successOutputs;
  private ArrayList<String> errorOutputs;
  private ArrayList<String> executedQueryScripts;
  private ArrayList<String> executedRollbackScripts;

  public ArrayList<String> getQueryScripts() {
    return queryScripts;
  }

  public void setQueryScripts(ArrayList<String> queryScripts) {
    this.queryScripts = queryScripts;
  }

  public ArrayList<String> getRollbackScripts() {
    return rollbackScripts;
  }

  public void setRollbackScripts(ArrayList<String> rollbackScripts) {
    this.rollbackScripts = rollbackScripts;
  }

  public ArrayList<?> getBeforeErrors() {
    return beforeErrors;
  }

  public void setBeforeErrors(ArrayList<?> beforeErrors) {
    this.beforeErrors = beforeErrors;
  }

  public ArrayList<?> getAfterErrors() {
    return afterErrors;
  }

  public void setAfterErrors(ArrayList<?> afterErrors) {
    this.afterErrors = afterErrors;
  }

  public ArrayList<ArrayList<?>> getSuccessOutputs() {
    return successOutputs;
  }

  public void setSuccessOutputs(ArrayList<ArrayList<?>> successOutputs) {
    this.successOutputs = successOutputs;
  }

  public ArrayList<String> getErrorOutputs() {
    return errorOutputs;
  }

  public void setErrorOutputs(ArrayList<String> errorOutputs) {
    this.errorOutputs = errorOutputs;
  }

  public ArrayList<String> getExecutedQueryScripts() {
    return executedQueryScripts;
  }

  public void setExecutedQueryScripts(ArrayList<String> executedQueryScripts) {
    this.executedQueryScripts = executedQueryScripts;
  }

  public ArrayList<String> getExecutedRollbackScripts() {
    return executedRollbackScripts;
  }

  public void setExecutedRollbackScripts(ArrayList<String> executedRollbackScripts) {
    this.executedRollbackScripts = executedRollbackScripts;
  }


}
