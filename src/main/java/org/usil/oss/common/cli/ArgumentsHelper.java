package org.usil.oss.common.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class ArgumentsHelper {

  public CommandLine getArguments(String args[]) throws Exception {
    Options options = new Options();

    Option o1 = new Option("h", "database_host", true, "database database host");
    o1.setRequired(true);
    options.addOption(o1);

    Option o2 = new Option("p", "database_port", true, "database database port");
    o2.setRequired(true);
    options.addOption(o2);

    Option o3 = new Option("name", "database_name", true, "database name");
    o3.setRequired(true);
    options.addOption(o3);

    Option o4 = new Option("user", "database_user", true, "database user");
    o4.setRequired(true);
    options.addOption(o4);

    Option o5 = new Option("pass", "database_password", true, "database password");
    o5.setRequired(true);
    options.addOption(o5);

    Option o6 = new Option("sf", "scripts_folder", true, "abolute path of folder with scripts");
    o6.setRequired(false);
    options.addOption(o6);

    Option o7 =
        new Option("en", "engine", true, "database engine. current support: oracle and mysql");
    o7.setRequired(false);
    options.addOption(o7);

    Option o8 = new Option("vl", "verbose_log", false, "show debug logs");
    o8.setRequired(false);
    options.addOption(o8);

    Option o9 = new Option("ddu", "database_dba_user", true, "user with dba permissions");
    o8.setRequired(false);
    options.addOption(o9);

    Option o10 = new Option("ddup", "database_dba_password", true, "password of dba user");
    o8.setRequired(false);
    options.addOption(o10);

    CommandLineParser parser = new DefaultParser();
    HelpFormatter formatter = new HelpFormatter();
    CommandLine cmd;

    try {
      cmd = parser.parse(options, args);
      return cmd;
    } catch (Exception e) {
      formatter.printHelp("dbvops", options);
      throw new Exception("Failed to receive the arguments.", e);
    }
  }
}
