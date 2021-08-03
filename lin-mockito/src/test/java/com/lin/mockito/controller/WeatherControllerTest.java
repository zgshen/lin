package com.lin.mockito.controller;

import com.lin.mockito.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService service;

    @Autowired
    private RestTemplate restTemplate;

    MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.createServer(restTemplate);
        //mock 三方接口数据
        server.expect(requestTo("https://free-api.heweather.com/v5/weather?key=0e551ab59fa34785af066024763673bd&city" +
                "=guangzhou"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"HeWeather5-123s\":[{\"aqi\":{\"city\":{\"aqi\":\"30\",\"co\":\"0.7\"," +
                                "\"no2\":\"14\",\"o3\":\"94\",\"pm10\":\"24\",\"pm25\":\"12\",\"qlty\":\"优\"," +
                                "\"so2\":\"6\"}},\"basic\":{\"city\":\"广州\",\"cnty\":\"中国\",\"id\":\"CN101280101\"," +
                                "\"lat\":\"23.125177\",\"lon\":\"113.280640\",\"update\":{\"loc\":\"2021-08-03 " +
                                "11:32\",\"utc\":\"2021-08-03 03:32\"}}," +
                                "\"daily_forecast\":[{\"astro\":{\"mr\":\"01:14\",\"ms\":\"14:55\",\"sr\":\"05:58\"," +
                                "\"ss\":\"19:08\"},\"cloud\":\"70\",\"cond\":{\"code_d\":\"306\",\"code_n\":\"302\"," +
                                "\"txt_d\":\"中雨\",\"txt_n\":\"雷阵雨\"},\"date\":\"2021-08-03\",\"hum\":\"78\"," +
                                "\"pcpn\":\"6.3\",\"pop\":\"70\",\"pres\":\"996\",\"tmp\":{\"max\":\"34\"," +
                                "\"min\":\"25\"},\"uv\":\"6\",\"vis\":\"21\",\"wind\":{\"deg\":\"0\",\"dir\":\"北风\"," +
                                "\"sc\":\"1-2\",\"spd\":\"3\"}},{\"astro\":{\"mr\":\"01:55\",\"ms\":\"15:49\"," +
                                "\"sr\":\"05:58\",\"ss\":\"19:07\"},\"cloud\":\"59\",\"cond\":{\"code_d\":\"306\"," +
                                "\"code_n\":\"302\",\"txt_d\":\"中雨\",\"txt_n\":\"雷阵雨\"},\"date\":\"2021-08-04\"," +
                                "\"hum\":\"86\",\"pcpn\":\"1.5\",\"pop\":\"59\",\"pres\":\"995\"," +
                                "\"tmp\":{\"max\":\"35\",\"min\":\"26\"},\"uv\":\"7\",\"vis\":\"16\"," +
                                "\"wind\":{\"deg\":\"0\",\"dir\":\"北风\",\"sc\":\"1-2\",\"spd\":\"3\"}}," +
                                "{\"astro\":{\"mr\":\"02:41\",\"ms\":\"16:42\",\"sr\":\"05:59\",\"ss\":\"19:07\"}," +
                                "\"cloud\":\"66\",\"cond\":{\"code_d\":\"306\",\"code_n\":\"302\",\"txt_d\":\"中雨\"," +
                                "\"txt_n\":\"雷阵雨\"},\"date\":\"2021-08-05\",\"hum\":\"81\",\"pcpn\":\"7.0\"," +
                                "\"pop\":\"66\",\"pres\":\"993\",\"tmp\":{\"max\":\"33\",\"min\":\"26\"}," +
                                "\"uv\":\"5\",\"vis\":\"21\",\"wind\":{\"deg\":\"0\",\"dir\":\"北风\",\"sc\":\"1-2\"," +
                                "\"spd\":\"3\"}}],\"now\":{\"cond\":{\"code\":\"104\",\"txt\":\"阴\"},\"dew\":\"26\"," +
                                "\"fl\":\"33\",\"hum\":\"72\",\"pcpn\":\"0.0\",\"pres\":\"994\",\"tmp\":\"31\"," +
                                "\"vis\":\"22\",\"wind\":{\"deg\":\"0\",\"dir\":\"北风\",\"sc\":\"3\",\"spd\":\"16\"}}," +
                                "\"status\":\"ok\",\"suggestion\":{\"air\":{\"brf\":\"良\"," +
                                "\"txt\":\"气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。\"},\"comf\":{\"brf\":\"非常不舒适\"," +
                                "\"txt\":\"白天虽然有少量降雨，但仍无法削弱高温带来的酷热，同时降雨造成湿度加大会您感到闷热难耐，请注意防暑。\"}," +
                                "\"cw\":{\"brf\":\"不宜\",\"txt\":\"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。\"}," +
                                "\"drsg\":{\"brf\":\"炎热\",\"txt\":\"天气炎热，建议着短衫、短裙、短裤、薄型T恤衫等清凉夏季服装。\"}," +
                                "\"flu\":{\"brf\":\"少发\",\"txt\":\"各项气象条件适宜，发生感冒机率较低。但请避免长期处于空调房间中，以防感冒。\"}," +
                                "\"sport\":{\"brf\":\"较不宜\",\"txt\":\"有扬沙或浮尘，建议适当停止户外运动，选择在室内进行运动，以避免吸入更多沙尘，有损健康。\"}," +
                                "\"trav\":{\"brf\":\"一般\",\"txt\":\"微风，感觉比较热，有降水，降雨期间请尽量不要外出，若外出，请注意防雷。\"}," +
                                "\"uv\":{\"brf\":\"中等\",\"txt\":\"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA" +
                                "+的防晒护肤品，戴帽子、太阳镜。\"}}}]}",
                        MediaType.APPLICATION_JSON));

        //请求返回自定义数据
        ResponseEntity<String> entity = restTemplate.getForEntity("https://free-api.heweather" +
                ".com/v5/weather?key=0e551ab59fa34785af066024763673bd&city=guangzhou", String.class);
        server.verify();

        //Mockito.when(service.RemoteWeatherResquest()).thenReturn(entity.getBody());
        Mockito.doReturn(entity.getBody()).when(service).RemoteWeatherResquest();
    }

    @Test
    void getWeatherInfo() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/getWeatherInfo")
                .accept(MediaType.ALL))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        log.info(mvcResult.getResponse().getContentAsString());
    }

}