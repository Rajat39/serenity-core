package net.serenitybdd.rest.staging

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import net.serenity.test.utils.rules.TestCase
import net.serenitybdd.rest.staging.decorators.ResponseDecorated
import net.serenitybdd.rest.staging.rules.RestConfigurationAction
import net.serenitybdd.rest.staging.rules.RestConfigurationRule
import net.thucydides.core.steps.BaseStepListener
import org.junit.Rule
import spock.lang.Specification

import static net.serenitybdd.rest.staging.SerenityRest.*
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.matching
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching

/**
 * User: YamStranger
 * Date: 3/30/16
 * Time: 9:57 AM
 */
class WhenExecutingPatchRequestsFromSerenityRest extends Specification {

    @Rule
    def WireMockRule wire = new WireMockRule(0);

    @Rule
    def RestConfigurationRule rule = new RestConfigurationRule(new RestConfigurationAction() {
        @Override
        void apply() {
            reset()
        }
    },)

    @Rule
    def TestCase<BaseStepListener> test = new TestCase({
        Mock(BaseStepListener)
    }.call());

    def "should return wrapped response during PATCH by URL called from Serenity Rest"() {
        given: "configured access point"
            def body = "<root>" +
                "<value>2</value>" +
                "</root>"
            def base = "http://localhost:${wire.port()}"
            def path = "/test/get/keyboard"
            def url = "$base$path"
            stubFor(WireMock.patch(urlMatching(path))
                .withRequestBody(matching(".*"))
                .willReturn(aResponse()
                .withStatus(256)
                .withHeader("Content-Type", "application/xml")
                .withBody(body)))
        when: "creating new request and making get request"
            def response = patch(new URL(url))
        then: "created response should be decorated"
            response instanceof ResponseDecorated
        and: "returned status should be correct"
            response.then().statusCode(256)
    }

    def "should return wrapped response during PATCH by URI called from Serenity Rest"() {
        given: "configured access point"
            def body = "<root>" +
                "<value>2</value>" +
                "</root>"
            def base = "http://localhost:${wire.port()}"
            def path = "/test/get/keyboard"
            def url = "$base$path"
            stubFor(WireMock.patch(urlMatching(path))
                .withRequestBody(matching(".*"))
                .willReturn(aResponse()
                .withStatus(256)
                .withHeader("Content-Type", "application/xml")
                .withBody(body)))
        when: "creating new request and making get request"
            def response = patch(new URI(url))
        then: "created response should be decorated"
            response instanceof ResponseDecorated
        and: "returned status should be correct"
            response.then().statusCode(256)
    }

    def "should return wrapped response during PATCH by base URL called from Serenity Rest"() {
        given: "configured access point"

            def body = "<root>" +
                "<value>2</value>" +
                "</root>"
            def base = "http://localhost"
            def path = "/test/get/keyboard"
            def url = "$base$path"
            stubFor(WireMock.patch(urlMatching(".*"))
                .withRequestBody(matching(".*"))
                .willReturn(aResponse()
                .withStatus(256)
                .withHeader("Content-Type", "application/xml")
                .withBody(body)))
            setDefaultRootPath(url)
            setDefaultPort(wire.port())
        when: "creating new request and making get request"
            def response = patch()
        then: "created response should be decorated"
            response instanceof ResponseDecorated
        and: "returned status should be correct"
            response.then().statusCode(256)
    }

    def "should return wrapped response during PATCH by path and parameters called from Serenity Rest"() {
        given: "configured access point"

            def body = "<root>" +
                "<value>2</value>" +
                "</root>"
            def base = "http://localhost:${wire.port()}"
            def path = "/test/get/path"
            def url = "$base$path"
            stubFor(WireMock.patch(urlMatching("$path.*"))
                .withRequestBody(matching(".*"))
                .willReturn(aResponse()
                .withStatus(256)
                .withHeader("Content-Type", "application/xml")
                .withBody(body)))
        when: "creating new request and making get request"
            def response = patch("$url?status={status}", ["status": "available"])
        then: "created response should be decorated"
            response instanceof ResponseDecorated
        and: "returned status should be correct"
            response.then().statusCode(256)
    }
}