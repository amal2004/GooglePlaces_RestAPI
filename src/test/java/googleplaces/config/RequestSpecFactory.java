package googleplaces.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

/** Creates a clean, consistently configured request specification for each scenario. */
public final class RequestSpecFactory {
    private RequestSpecFactory() {
    }

    public static RequestSpecification create() {
        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("baseUrl"))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParam("key", ConfigReader.get("apiKey"))
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter());
        return builder.build();
    }
}
