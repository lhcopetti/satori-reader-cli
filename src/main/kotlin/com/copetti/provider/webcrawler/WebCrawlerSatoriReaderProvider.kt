package com.copetti.provider.webcrawler

import com.copetti.core.gateway.SatoriReaderProvider
import com.copetti.core.gateway.SatoriReaderProviderRequest
import com.copetti.model.SatoriReaderEdition
import com.copetti.model.SatoriReaderEpisode
import com.copetti.model.SatoriReaderSeries
import com.copetti.model.SatoriReaderStatus
import com.copetti.model.SatoriReaderWebConstants.SATORI_READER_URL
import com.copetti.model.SatoriReaderWebConstants.SERIES_URL
import com.copetti.model.SatoriReaderWebConstants.SIGNIN_URL
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class WebCrawlerSatoriReaderProvider : SatoriReaderProvider {
    override fun fetchAllSeries(request: SatoriReaderProviderRequest): List<SatoriReaderSeries> {
        val token = login(request)
        val seriesLinks = fetchSeriesLinks().take(1)
        return seriesLinks.map { link -> mapEpisodes(token, link) }
    }

    override fun resetReadingProgress(request: SatoriReaderProviderRequest) {
        throw NotImplementedError()
    }

    private fun fetchSeriesLinks(): List<String> {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(SATORI_READER_URL + SERIES_URL)
            .build()
        val response = client.newCall(request).execute()

        val document = Jsoup.parse(response.body?.string() ?: "")

        val linkElements = document.getElementsByClass("series-tiles")
            .first()
            ?.getElementsByTag("a") ?: listOf()

        return linkElements
            .map { element -> element.attribute("href")?.value ?: "" }
            .filterNot(String::isEmpty)
            .toList()
    }

    private fun mapEpisodes(token: String, seriesLink: String): SatoriReaderSeries {

        val response: String = OkHttpClient().newCall(
            Request.Builder()
                .url(SATORI_READER_URL + seriesLink)
                .header("Cookie", "SessionToken=$token")
                .build()
        ).execute().body?.string() ?: ""
        val document = Jsoup.parse(response)
        val seriesTitle = document.getElementsByClass("series-detail-title-description-area")
            .first()
            ?.getElementsByTag("h1")
            ?.text() ?: ""
        val episodes = document.getElementsByClass("series-detail-grid-item")
            .map { episode -> mapEpisodes(episode) }

        return SatoriReaderSeries(title = seriesTitle, episodes = episodes)
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
                    url = url,
                    status = status
                )
            } ?: listOf()
    }

    private fun login(request: SatoriReaderProviderRequest): String {
        val formBody = FormBody.Builder()
            .add("username", request.credentials.username)
            .add("password", request.credentials.password)
            .build()

        val signInRequest = Request.Builder()
            .url(SATORI_READER_URL + SIGNIN_URL)
            .post(formBody)
            .build()

        val client = OkHttpClient.Builder()
            .followRedirects(false)
            .build()

        val response = client.newCall(signInRequest).execute()
        return getSessionTokenFromResponse(response)
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

}