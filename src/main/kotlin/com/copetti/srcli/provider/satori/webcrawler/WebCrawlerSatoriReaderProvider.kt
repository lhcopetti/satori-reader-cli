package com.copetti.srcli.provider.satori.webcrawler

import com.copetti.srcli.domain.gateway.SatoriReaderProvider
import com.copetti.srcli.domain.model.*
import com.copetti.srcli.provider.satori.SatoriReaderWebConstants.SATORI_READER_API_URL
import com.copetti.srcli.provider.satori.SatoriReaderWebConstants.SATORI_READER_URL
import com.copetti.srcli.provider.satori.SatoriReaderWebConstants.SERIES_URL
import com.copetti.srcli.provider.satori.SatoriReaderWebConstants.SIGNIN_URL
import mu.KotlinLogging
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class WebCrawlerSatoriReaderProvider : SatoriReaderProvider {

    private val logger = KotlinLogging.logger { }

    override fun login(credentials: SatoriReaderCredentials): SatoriReaderLoginToken {
        logger.info { "SATORI_READER_LOGIN_REQUEST" }
        val formBody = FormBody.Builder()
            .add("username", credentials.username)
            .add("password", credentials.password)
            .build()

        val signInRequest = Request.Builder()
            .url(SATORI_READER_URL + SIGNIN_URL)
            .post(formBody)
            .build()

        val client = OkHttpClient.Builder()
            .followRedirects(false)
            .build()

        val loginResponse = client.newCall(signInRequest).execute()
        logger.info { "SATORI_READER_LOGIN_RESPOSE | status: ${loginResponse.code}" }
        val token = getSessionTokenFromResponse(loginResponse)
        return SatoriReaderLoginToken(sessionToken = token)
    }

    private fun getSessionTokenFromResponse(currentResponse: Response): String {
        val cookie = currentResponse.header("Set-Cookie")
        val headers: List<String> = cookie?.split(";") ?: listOf()
        for (header in headers) {
            val comp = header.split("=")
            if (comp[0] == "SessionToken")
                return comp[1]
        }

        throw IllegalStateException("Token not found in cookie header")
    }

    override fun fetchSeries(): List<SatoriReaderSeriesReference> {
        logger.info { "SATORI_READER_FETCH_SERIES_REQUEST" }
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(SATORI_READER_URL + SERIES_URL)
            .build()
        val response = client.newCall(request).execute()
        logger.info { "SATORI_READER_FETCH_SERIES_RESPONSE | status: ${response.code}" }

        val document = Jsoup.parse(response.body?.string() ?: "")

        val linkElements = document.getElementsByClass("series-tiles")
            .first()
            ?.getElementsByTag("a") ?: listOf()

        return linkElements
            .map { element -> element.attribute("href")?.value ?: "" }
            .filterNot(String::isEmpty)
            .map { link -> SatoriReaderSeriesReference(link = SATORI_READER_URL + link) }
            .toList()
            .also { logger.info { "SATORI_READER_FETCH_SERIES | seriesCount: ${it.size}" } }
    }

    override fun fetchSeriesContent(request: FetchSeriesContentRequest): SatoriReaderSeriesContent {
        logger.info { "SATORI_READER_FETCH_SERIES_CONTENT_REQUEST | series: ${request.series}" }

        val response = OkHttpClient().newCall(
            Request.Builder()
                .url(request.series.link)
                .header("Cookie", "SessionToken=${request.token.sessionToken}")
                .build()
        ).execute()
        logger.info { "SATORI_READER_FETCH_SERIES_CONTENT_RESPONSE | status: ${response.code}" }

        val body: String = response.body?.string() ?: ""
        val document = Jsoup.parse(body)
        val seriesTitle = document.getElementsByClass("series-detail-title-description-area")
            .first()
            ?.getElementsByTag("h1")
            ?.text() ?: ""
        val episodes = document.getElementsByClass("series-detail-grid-item")
            .map { episode -> mapEpisodes(episode) }

        return SatoriReaderSeriesContent(title = seriesTitle, link = request.series.link, episodes = episodes)
    }

    override fun resetReadingProgress(request: ResetEditionReadingProgressRequest) {
        logger.info { "SATORI_READER_RESET_PROGRESS_REQUEST | editionUrl: ${request.edition.urlPath}" }

        OkHttpClient().newCall(
            Request.Builder()
                .url(SATORI_READER_API_URL + request.edition.urlPath + "/status")
                .header("Cookie", "SessionToken=${request.token.sessionToken}")
                .put("""{"type":"read-state","value":"0"}""".toRequestBody())
                .build()
        ).execute()
            .also { logger.info { "SATORI_READER_RESET_PROGRESS_RESPONSE | status: ${it.code}" } }
    }

    private fun mapEpisodes(episode: Element): SatoriReaderEpisode {
        val title = episode.getElementsByClass("title").text()
        val editions: List<SatoriReaderEdition> = mapEditions(episode)
        return SatoriReaderEpisode(title = title, editions = editions)
    }

    private fun mapEditions(episode: Element): List<SatoriReaderEdition> {
        return episode.getElementsByTag("table")
            .first()
            ?.getElementsByTag("tr")
            ?.drop(1)
            ?.map { row ->
                val columns = row.getElementsByTag("td")
                val name = columns[0].text()
                val url = columns[0].getElementsByTag("a")
                    .first()
                    ?.attribute("href")?.value ?: ""
                val status = SatoriReaderStatus.entries
                    .first { st -> st.toString().equals(columns[1].text(), ignoreCase = true) }
                SatoriReaderEdition(
                    name = name,
                    urlPath = url,
                    link = SATORI_READER_URL + url,
                    status = status
                )
            } ?: listOf()
    }

}