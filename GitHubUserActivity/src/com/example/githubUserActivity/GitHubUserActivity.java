package com.example.githubUserActivity;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.stream.JsonParsingException;

public class GitHubUserActivity {

    // Event type constants
    private static final String TYPE_PUSH = "PushEvent";
    private static final String TYPE_CREATE = "CreateEvent";
    private static final String TYPE_DELETE = "DeleteEvent";
    private static final String TYPE_WATCH = "WatchEvent";


    public GitHubUserActivity(String username) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/users/" + username + "/events"))
            .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("HTTP request failed: " + e.getMessage());
            return;
        }

        if (response == null) {
            System.err.println("No response received from GitHub API.");
            return;
        }

        int status = response.statusCode();
        String output = response.body();

        if (status == 200) {
            try (JsonReader reader = Json.createReader(new StringReader(output))) {
                JsonArray events = reader.readArray();

                System.out.println("Response:");

                for (JsonValue eventVal : events) {
                    JsonObject event = eventVal.asJsonObject();
                    String type = event.getString("type", "");

                    switch (type) {
	                    case TYPE_PUSH: pushEvent(event); break;
	                    //PullRequestEvent
		                //IssuesEvent
		                //IssueCommentEvent	
	                    case TYPE_CREATE: createEvent(event); break;
	                    case TYPE_DELETE: deleteEvent(event); break;
	                    //ForkEvent
	                    case TYPE_WATCH: watchEvent(event); break;
	                    //ReleaseEvent
		                //PullRequestReviewEvent
		                //PullRequestReviewCommentEvent
		                //MemberEvent
		                //PublicEvent
		                //GollumEvent
	                    default:
	                        System.out.println("- Event type '" + type + "' is not yet handled.");
	                        break;
                    }
                }
            } catch (JsonParsingException e) {
                System.err.println("Invalid JSON received: " + e.getMessage());
            }
        } else if (status == 404) {
            System.out.println("User not found. Make sure the username is correct.");
        } else {
            System.out.println("Unexpected HTTP status: " + status);
        }

//        System.out.println("DEBUG: " + output);
    }

    private void pushEvent(JsonObject event) {
        JsonObject payload = event.getJsonObject("payload");
        if (payload != null) {
            System.out.println("- Pushed " + payload.getInt("size", 0) + " commit(s) to " + getRepoName(event));
        }
    }

    private void createEvent(JsonObject event) {
        JsonObject payload = event.getJsonObject("payload");
        if (payload != null) {
            String refType = payload.getString("ref_type", "");
            if ("repository".equals(refType)) {
                System.out.println("- Created repository " + getRepoName(event));
            } else {
                // Only print ref if it's not empty
                String ref = payload.getString("ref", "");
                String refDisplay = ref.isEmpty() ? "" : " " + ref;
                System.out.println("- Created " + refType + refDisplay + " on " + getRepoName(event));
            }
        }
    }

    private void deleteEvent(JsonObject event) {
        JsonObject payload = event.getJsonObject("payload");
        if (payload != null) {
            System.out.println("- Deleted " + payload.getString("ref_type", "") + " " + payload.getString("ref", "") + " on " + getRepoName(event));
        }
    }

    private void watchEvent(JsonObject event) {
        System.out.println("- Starred " + getRepoName(event));
    }

    // Utility method
    private String getRepoName(JsonObject event) {
        JsonObject repo = event.getJsonObject("repo");
        return (repo != null) ? repo.getString("name", "unknown") : "unknown";
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Incorrect number of arguments!");
            return;
        }
        String username = args[0];
        new GitHubUserActivity(username);
    }
}

//