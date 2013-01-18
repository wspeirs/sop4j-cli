package com.sop4j;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;


public class RequiredOptions {

    public static void main(String[] args) {
        System.out.println("*** CALL HELP ***");
        fakeMain(new String[] { "-h" });
        System.out.println();

        System.out.println("*** PASS FILE ARG ***");
        fakeMain(new String[] { "--file", "myFile" });
        System.out.println();
        
        System.out.println("*** PASS NOTHING ***");
        fakeMain(new String[] { });
        System.out.println();
    }
    
    public static void fakeMain(String[] args) {
        final GnuParser parser = new GnuParser();
        final Options options = configureOptions();
        
        // parse first for our help option
        CommandLine cmdLine = null;
        try {
            cmdLine = parser.parse(new Options().addOption(new Option("h", "help", false, "Show this help")), args);

            // check to see if they need help
            // notice we do this here as it could have thrown an UnrecognizedOptionException
            if(cmdLine.hasOption("h")) {
                new HelpFormatter().printHelp("sop4j-cli", options);
                return;
            }
        } catch(UnrecognizedOptionException e) {
            // do nothing here, as they could be passing other options besides help
        } catch(ParseException e) {
            e.printStackTrace(); // something else went wrong
            return;
        }
        
        // then parse again with the rest of the options
        try {
            cmdLine = parser.parse(options, args);
        } catch(ParseException e) {
            e.printStackTrace();
            return;
        }

        // get the file option, no need to check as it's required
        final String file = cmdLine.getOptionValue("file");
        System.out.println("File option: " + file);
    }
    
    @SuppressWarnings("static-access")
    public static Options configureOptions() {
        Options options = new Options();

        // an option with an argument, no short version, and is required
        options.addOption(OptionBuilder.withLongOpt("file")
                                       .withArgName("input-file") // also need hasArg() or won't work
                                       .hasArg()
                                       .withDescription("The input file")
                                       .isRequired()
                                       .create());
        
        return options;
    }
}
