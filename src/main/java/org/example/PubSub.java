package org.example;

import org.apache.commons.cli.*;
import redis.clients.jedis.RedisClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PubSub {

    static void main(String[] args) {
        Options options = new Options();
        options.addOption("p", "pub", false, "Redis publisher");
        options.addOption("s", "sub", false, "Redis subscriber");
        options.addRequiredOption("h", "host", true, "Redis host");
        options.addRequiredOption("P", "port", true, "Redis port");
        options.addOption(Option.builder("c").longOpt("channel").hasArgs().required().desc("Redis channel(s)").get());
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            Worker worker = new Worker();
            String host = cmd.getOptionValue("host");
            String port = cmd.getOptionValue("port");
            String redisURI = "redis://" + host + ":" + port;
            String[] channels = cmd.getOptionValues("channel");
            try (RedisClient client = new RedisClient.Builder().fromURI(redisURI).build()) {
                if (cmd.hasOption("pub")) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        System.out.println("Enter message: ");
                        String message = in.readLine();
                        if (message == null) {
                            break;
                        }
                        for (String channel : channels) {
                            client.publish(channel, message);
                            System.out.println("Message published to channel: " + channel);
                        }
                    }
                } else if (cmd.hasOption("sub")) {
                    System.out.println("Redis subscriber listening on channels: " + String.join(", ", channels));
                    client.subscribe(worker, channels);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}