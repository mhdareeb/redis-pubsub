package org.example;

import org.apache.commons.cli.*;

public class RedisConfig {

    private final String host;
    private final int port;
    private final String[] channels;
    private final boolean isPublisher;
    private final boolean isSubscriber;
    private static final Options options = new Options();

    static {
        options.addOption("p", "pub", false, "Redis publisher");
        options.addOption("s", "sub", false, "Redis subscriber");
        options.addRequiredOption("h", "host", true, "Redis host");
        options.addRequiredOption("P", "port", true, "Redis port");
        options.addOption(Option.builder("c").longOpt("channel").hasArgs().required().desc("Redis channel(s)").get());
    }

    private boolean isValidConfig() {
        return host != null
                && port > 0
                && channels != null && channels.length > 0
                && (isPublisher || isSubscriber);
    }

    public RedisConfig(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            this.host = cmd.getOptionValue("host");
            this.port = Integer.parseInt(cmd.getOptionValue("port"));
            this.channels = cmd.getOptionValues("channel");
            this.isPublisher = cmd.hasOption("pub");
            this.isSubscriber = cmd.hasOption("sub");
            if (!isValidConfig()) {
                throw new RuntimeException("Invalid configuration");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getURI() {
        return "redis://" + host + ":" + port;
    }

    public String[] getChannels() {
        return channels;
    }

    public boolean isPublisher() {
        return isPublisher;
    }

    public boolean isSubscriber() {
        return isSubscriber;
    }

}