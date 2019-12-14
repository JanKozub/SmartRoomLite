package org.jk.smlite.cli.model;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCommand implements Command {
    protected static final Option REPEAT = Option.builder("r").longOpt("repeat").hasArg().build();

    private final String name;
    private final String description;

    protected AbstractCommand(String name) {
        this(name, name + " command");
    }

    protected AbstractCommand(String name, String description) {
        Set<String> loggers = new HashSet<>(Arrays.asList("org.apache.http", "groovyx.net.http"));

        for (String log : loggers) {
            Logger logger = (Logger) LoggerFactory.getLogger(log);
            logger.setLevel(Level.INFO);
            logger.setAdditive(false);
        }
        this.name = name;
        this.description = description;
    }

    protected static String sendGET(String url) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } else {
                return "GET request not worked";
            }
        } catch (IOException ex) {
            return "";
        }
    }

    public static void sendPOST(String completeUrl, String body) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(completeUrl);
        httpPost.setHeader("Content-type", "application/json");
        try {
            StringEntity stringEntity = new StringEntity(body);
            httpPost.getRequestLine();
            httpPost.setEntity(stringEntity);

            httpClient.execute(httpPost);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Options getOptions() {
        return new Options();
    }

    @Override
    public void execute(Context context) {
        throw new UnsupportedOperationException("Command not supported");
    }

    protected void executeGet(Context context) {
        repeatIfPresent(context.getCmdLine(), () -> {
            String response = sendGET(context.getUrl() + "/switch/getState/" + context.getCmdLine().getArgList().get(0));
            try {
                context.getOut().write(response);
                context.getOut().newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected void executeSwitch(Context context) {
        repeatIfPresent(context.getCmdLine(), () -> {
            sendPOST(context.getUrl() + "/switch" + "/setState", context.getCmdLine().getArgList().get(0));
        });
    }

    protected void repeatIfPresent(CommandLine cl, Runnable r) {
        if (cl.hasOption(REPEAT.getOpt())) {
            int cnt = Integer.parseInt(cl.getOptionValue(REPEAT.getOpt()));
            for (int i = 0; i < cnt; i++)
                r.run();
        } else
            r.run();
    }
}
