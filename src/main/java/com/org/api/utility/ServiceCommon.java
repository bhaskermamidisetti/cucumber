package com.org.api.utility;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Utility class containing common methods related to restassured.
 */
public final class ServiceCommon {


    /**
     * Private constructor for utility class.
     */
    private ServiceCommon() {
    }

    /**
     * Base URL.
     */
    private static String baseURL;

    /**
     * baseURL getter method.
     *
     * @return - String
     */
    public static String getBaseURL() {
        return baseURL;
    }

    /**
     * baseURL setter method.
     *
     * @param baseUrl - Base url.
     */
    public static void setBaseURI(final String baseUrl) {
        ServiceCommon.baseURL = baseUrl;
    }

    /**
     * Verifies the status code of the response.
     *
     * @param pathToResource - Path to the resource.
     * @param statusCode     - Status code to be checked.
     */
    public static void checkStatusCode(final String pathToResource, final int statusCode) {
        given().when().get(getBaseURL() + pathToResource).then().assertThat().statusCode(statusCode);
    }

    /**
     * Checks the value for a certain key in json.
     *
     * @param pathToResource - Path to resourse.
     * @param jpath          - Jpath to locate the key.
     * @param data           - Value to be verified for that key.
     */
    public static void checkContentInJson(final String pathToResource, final String jpath, final String data) {
        given().header("Accept", "application/json").when().get(getBaseURL() + pathToResource).then().body(jpath,
                equalTo(data));
    }

    /**
     * Checks the value for a certain key in xml.
     *
     * @param pathToResource - Path to resource.
     * @param xmlPath        - xml path to locate the key.
     * @param data           - Value to be verified for that key.
     */
    public static void checkContentInXml(final String pathToResource, final String xmlPath, final String data) {
        given().header("Accept", "application/xml").when().get(getBaseURL() + pathToResource).then().body(xmlPath,
                equalTo(data));
    }

    /**
     * This method verifies a cookie.
     *
     * @param pathToResource - Path to resource.
     * @param cookieKey      - Key value of cookie.
     * @param cookieValue    - Value of that cookie.
     */
    public static void verifyCookies(final String pathToResource, final String cookieKey, final String cookieValue) {
        given().when().get(getBaseURL() + pathToResource).then().assertThat().cookie(cookieKey, cookieValue);
    }

    /**
     * This method verifies header value.
     *
     * @param pathToResource - Path to resource.
     * @param headerKey      - Key of the header.
     * @param headerValue    - Value of the header.
     */
    public static void verifyHeaders(final String pathToResource, final String headerKey, final String headerValue) {
        given().when().get(getBaseURL() + pathToResource).then().assertThat().header(headerKey, headerValue);
    }

    /**
     * This method verifies the content type of the request.
     *
     * @param pathToResource - Path to the resource.
     * @param contentType    - Content type string.
     */
    public static void verifyContentType(final String pathToResource, final String contentType) {
        given().when().get(getBaseURL() + pathToResource).then().assertThat().contentType(contentType);
    }

    /**
     * This method verifies the content type of the request.
     *
     * @param pathToResource - Path to the resource.
     * @param contentType    - Content type.
     */
    public static void verifyContentType(final String pathToResource, final ContentType contentType) {
        given().when().get(getBaseURL() + pathToResource).then().assertThat().contentType(contentType);
    }

    /**
     * This method returns a ValidateResponce object which can be used to perform
     * chain of assertions.
     *
     * @param pathToResource - Path to resource.
     * @return - ValidatableResponce.
     */
    public static ValidatableResponse assertChain(final String pathToResource) {
        return given().when().get(getBaseURL() + pathToResource).then().assertThat();
    }

}
