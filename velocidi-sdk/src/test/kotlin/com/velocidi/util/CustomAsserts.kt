package com.velocidi.util

import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions

fun RecordedRequest.containsHeader(header: String, expectedHeader: String) {
    val actualHeader = this.getHeader(header)
    Assertions.assertThat(actualHeader)
        .isNotNull()
        .isNotEmpty()
        .overridingErrorMessage(
            "Expected header to be <%s> but got <%s>.",
            expectedHeader,
            actualHeader
        )
        .isEqualTo(expectedHeader)
}

fun RecordedRequest.containsRequestLine(expectedReqLine: String) {
    Assertions.assertThat(this.requestLine)
        .isNotNull()
        .isNotEmpty()
        .overridingErrorMessage(
            "Expected header to be <%s> but got <%s>.",
            expectedReqLine,
            this.requestLine
        )
        .isEqualTo(expectedReqLine)
}

fun RecordedRequest.containsBody(expectedBody: String) {
    val body = String(this.body.readByteArray())
    Assertions.assertThat(body)
        .isNotNull()
        .isNotEmpty()
        .overridingErrorMessage(
            "Expected body to be <%s> but got <%s>.",
            expectedBody,
            body
        )
        .isEqualTo(expectedBody)
}
