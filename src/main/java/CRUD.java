import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CRUD {
    private CloseableHttpClient httpClient;
    CloseableHttpResponse response;
    private int connectTimeout = 5000;
    private int socketTimeout = 30000;
    private Map<String, String> url = new HashMap<>();
    private String json;
    private List<Header> list;
    private Map<String, String> headers = new HashMap<>();
    private String domen;
    private String userAgent = "MyCRUD";
    public static final String READ_ALL = "READ_ALL";
    public static final String READ_PAGE = "READ_PAGE";
    public static final String READ_ONE = "READ_ONE";
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String DELETE = "DELETE";
    public static final String STATUS = "Status";
    public static final String OK = "200";
    public static final String X_TOTAL_COUNT = "X-Pagination-Total-Count";
    public static final String X_PAGE_COUNT = "X-Pagination-Page-Count";
    public static final String X_CURRENT_PAGE = "X-Pagination-Current-Page";
    public static final String X_PER_PAGE = "X-Pagination-Per-Page";


    public CRUD(String domen, Map<String, String> url) {
        if (domen.endsWith("/")) {
            this.domen = domen;
        } else {
            this.domen = domen.concat("/");
        }
        this.url.putAll(url);
        try {
            httpClient = HttpClientBuilder.create()
                    .setUserAgent(userAgent)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(connectTimeout)  // максимальное время ожидание подключения к серверу
                            .setSocketTimeout(socketTimeout)    // максимальное время ожидания получения данных
                            .setRedirectsEnabled(false)         // возможность следовать редиректу в ответе
                            .build())
                    .build();
        } catch (Exception err) {
            err.getStackTrace();
        }
    }

    public CRUD readAll() throws IOException {
        HttpGet request = new HttpGet(domen + url.get(CRUD.READ_ALL));
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public CRUD readPage(int page) throws IOException {
        HttpGet request = new HttpGet(domen + url.get(CRUD.READ_PAGE) + page);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public CRUD readOne(int id) throws IOException {
        HttpGet request = new HttpGet(domen + url.get(CRUD.READ_ONE) + id);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public CRUD create(News news) throws IOException {
        HttpPost request = new HttpPost(domen + url.get(CRUD.CREATE));
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        request.setEntity(formEntity(news));
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public CRUD update(News news) throws IOException {
        HttpPut request = new HttpPut(domen + url.get(CRUD.UPDATE) + news.getId());
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        request.setEntity(formEntity(news));
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public CRUD delete(int id) throws IOException {
        HttpDelete request = new HttpDelete(domen + url.get(CRUD.DELETE) + id);
        request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
        try {
            response = httpClient.execute(request);
            response();
        } catch (Exception err) {
            err.getStackTrace();
        } finally {
            response.close();
        }
        return this;
    }

    public void getShowHeaders() {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public String getJson() {
        return headers.get(STATUS).equals(OK) ? json : headers.get(STATUS);
    }

    private void response() throws Exception{
        json = null;
        list = null;
        headers.clear();
        json = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
        list = Stream.of(response.getAllHeaders()).collect(Collectors.toList());
        for (Header key : list) {
            headers.put(key.getName(), key.getValue());
        }
        if (response.getStatusLine().toString().contains(OK)) {
            headers.put(STATUS, OK);
        } else {
            headers.put(STATUS, response.getStatusLine().toString());
        }
    }

    public boolean pagination() {
        if (!headers.containsKey(X_PAGE_COUNT) && !headers.containsKey(X_PAGE_COUNT) &&
                !headers.containsKey(X_PAGE_COUNT) && !headers.containsKey(X_PAGE_COUNT)) {
            return false;
        }
        int pageCount = Integer.parseInt(headers.get(X_PAGE_COUNT)); // всего страниц
        int currentPage = Integer.parseInt(headers.get(X_CURRENT_PAGE)); // текущая страница
        int perPage = Integer.parseInt(headers.get(X_PER_PAGE)); // количество новостей на странице
        int totalCount = Integer.parseInt(headers.get(X_TOTAL_COUNT)); // всего в базе новостей
        System.out.print("\nСтраницы: ");
        for (int i = 1; i <= pageCount; i++) {
            if (i == currentPage) {
                System.out.print("<" + i + ">");
            } else {
                System.out.print(" " + i + " ");
            }
        }
        int to = perPage * currentPage;
        System.out.println("  показано: " + (to - (perPage - 1)) + ".." + (to < totalCount ? to : totalCount) + " из " + totalCount);
        return true;
    }

    private UrlEncodedFormEntity formEntity(News news) throws UnsupportedEncodingException {
        // Объявление коллекции List для инкапсуляции параметров в форме
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("News[id]", String.valueOf(news.getId())));
        params.add(new BasicNameValuePair("News[name_news]", news.getNameNews()));
        params.add(new BasicNameValuePair("News[short_desc]", news.getShortDesc()));
        params.add(new BasicNameValuePair("News[full_desc]", news.getFullDesc()));
        params.add(new BasicNameValuePair("News[type_id]", String.valueOf(news.getTypeId())));
        params.add(new BasicNameValuePair("News[source]", news.getSource()));
        // Объект Entity формы
        return new UrlEncodedFormEntity(params,"utf8");
    }

}
