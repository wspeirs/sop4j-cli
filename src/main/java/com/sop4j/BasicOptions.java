package com.sop4j;

import java.util.Map.Entry;
import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class BasicOptions {

    public static void main(String[] args) {
        fakeMain(new String[] { "-h" });
        
        fakeMain(new String[] { "--opt", "-Dsop4j=true", "--file", "myFile", "--hostname", "www.sop4j.com" });
        
        fakeMain(new String[] { "--ip-address", "192.168.1.1", "--hostname", "www.sop4j.com"});
    }
    
    public static void fakeMain(String[] args) {
        final GnuParser parser = new GnuParser();
        final Options options = configureOptions();

        // parse with the parser
        CommandLine cmdLine = null;
        try {
            cmdLine = parser.parse(options, args);
        } catch(ParseException e) {
            e.printStackTrace();
            return;
        }
        
        // check to see if they need help
        // notice we only use the short-version, but it checks both!
        if(cmdLine.hasOption("h")) {
            new HelpFormatter().printHelp("sop4j-cli", options);
            return;
        }

        // check to see if they passed in an opt
        // recommended to use the long version as it's more readable
        if(cmdLine.hasOption("opt")) {
            System.out.println("Found opt!");
        }
        
        // get -D options and their values
        if(cmdLine.hasOption("D")) {
            Properties props = cmdLine.getOptionProperties("D");
            
            for(Entry<Object, Object> entry:props.entrySet()) {
                System.out.println("Property: " + entry.getKey() + " " + entry.getValue());
            }
        }

        // see if we have a file option by checking it's value
        String file = null;
        if(null != (file = cmdLine.getOptionValue("file")) ) {
            System.out.println("File option: " + file);
        }
        
        // get the IP address or hostname
        if(cmdLine.hasOption("ip-address")) {
            System.out.println("IP: " + cmdLine.getOptionValue("ip-address"));
        }
        if(cmdLine.hasOption("hostname")){
            System.out.println("Hostname: " + cmdLine.getOptionValue("hostname"));
        }
    }
    
    @SuppressWarnings("static-access")
    public static Options configureOptions() {
        Options options = new Options();
        
        // create the simple option without using the OptionBuilder
        options.addOption("o", "opt", false, "An option, no args");
        
        // simple help option using the builder
        options.addOption(OptionBuilder.withLongOpt("help")
                                       .create("h"));
        
        // an option with a value separator, defaults to =
        options.addOption(OptionBuilder.withValueSeparator()
                                       .withDescription("Java style property")
                                       .create("D"));
        
        // an option with an argument, no short version, and is required
        options.addOption(OptionBuilder.withLongOpt("file")
                                       .withArgName("input-file") // also need hasArg() or won't work
                                       .hasArg()
                                       .withDescription("The input file")
                                       .create());
        
        // create the group to specify either an IP or hostname
        OptionGroup group = new OptionGroup();
        
        group.addOption(OptionBuilder.withLongOpt("ip-address")
                                     .withArgName("ip")
                                     .hasArg()
                                     .withDescription("IP address to use")
                                     .create());
        
        group.addOption(OptionBuilder.withLongOpt("hostname")
                                     .withArgName("host")
                                     .hasArg()
                                     .withDescription("Hostname address to use")
                                     .create());
                
        options.addOptionGroup(group);
        
        return options;
    }
}
