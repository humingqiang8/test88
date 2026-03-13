import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * 杭州天气查询卡片
 * 使用高德地图天气API查询杭州实时天气
 * 注意：需要替换为您的高德地图API密钥
 */
public class HangzhouWeatherCard {
    
    // 高德地图天气API密钥（请替换为您自己的密钥）
    // 获取方式：https://lbs.amap.com/api/webservice/guide/api/weatherinfo
    private static final String API_KEY = "您的高德地图API_KEY";
    
    // 杭州的adcode（行政区划代码）
    private static final String HANGZHOU_ADCODE = "330100";
    
    // 天气API地址
    private static final String WEATHER_API_URL = 
        "https://restapi.amap.com/v3/weather/weatherInfo";
    
    /**
     * 查询杭州天气并显示卡片信息
     */
    public static void displayWeatherCard() {
        try {
            // 构建请求URL
            String urlString = WEATHER_API_URL + 
                "?city=" + HANGZHOU_ADCODE + 
                "&key=" + API_KEY + 
                "&extensions=base";
            
            // 发送HTTP请求
            String jsonResponse = sendHttpGetRequest(urlString);
            
            // 解析JSON响应
            JSONObject jsonObject = new JSONObject(jsonResponse);
            
            if ("1".equals(jsonObject.getString("status"))) {
                // 获取天气信息
                JSONArray lives = jsonObject.getJSONArray("lives");
                if (lives.length() > 0) {
                    JSONObject weather = lives.getJSONObject(0);
                    
                    // 提取天气信息
                    String province = weather.getString("province");
                    String city = weather.getString("city");
                    String weatherType = weather.getString("weather");
                    String temperature = weather.getString("temperature");
                    String windDirection = weather.getString("winddirection");
                    String windPower = weather.getString("windpower");
                    String humidity = weather.getString("humidity");
                    String reportTime = weather.getString("reporttime");
                    
                    // 显示天气卡片
                    printWeatherCard(province, city, weatherType, temperature, 
                                   windDirection, windPower, humidity, reportTime);
                } else {
                    System.out.println("未获取到天气信息");
                }
            } else {
                System.out.println("API请求失败: " + jsonObject.getString("info"));
            }
            
        } catch (Exception e) {
            System.out.println("查询天气时发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 打印格式化的天气卡片
     */
    private static void printWeatherCard(String province, String city, 
                                       String weatherType, String temperature,
                                       String windDirection, String windPower,
                                       String humidity, String reportTime) {
        
        // 获取天气图标
        String weatherIcon = getWeatherIcon(weatherType);
        
        // 打印卡片边框
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         🌤️  杭州天气卡片  🌤️           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  📍 地区：%-24s ║\n", province + " " + city);
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  🌡️ 天气：%-24s ║\n", weatherIcon + " " + weatherType);
        System.out.printf("║  🌡️ 温度：%-24s ║\n", temperature + "°C");
        System.out.printf("║  💨 风向：%-24s ║\n", windDirection + "风");
        System.out.printf("║  💨 风力：%-24s ║\n", windPower + "级");
        System.out.printf("║  💧 湿度：%-24s ║\n", humidity + "%");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  🕐 更新时间：%-20s ║\n", reportTime);
        System.out.println("╚════════════════════════════════════════╝");
    }
    
    /**
     * 根据天气类型返回对应的图标
     */
    private static String getWeatherIcon(String weatherType) {
        if (weatherType.contains("晴")) return "☀️";
        if (weatherType.contains("云")) return "⛅";
        if (weatherType.contains("雨")) return "🌧️";
        if (weatherType.contains("雪")) return "❄️";
        if (weatherType.contains("雾")) return "🌫️";
        if (weatherType.contains("雷")) return "⛈️";
        return "🌤️";
    }
    
    /**
     * 发送HTTP GET请求
     */
    private static String sendHttpGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // 设置请求方法
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        // 获取响应码
        int responseCode = connection.getResponseCode();
        
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // 读取响应内容
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return response.toString();
        } else {
            throw new Exception("HTTP请求失败，响应码: " + responseCode);
        }
    }
    
    /**
     * 主方法 - 测试天气卡片
     */
    public static void main(String[] args) {
        System.out.println("正在查询杭州天气...\n");
        
        // 检查API密钥是否已配置
        if ("您的高德地图API_KEY".equals(API_KEY)) {
            System.out.println("⚠️  警告：请先配置高德地图API密钥！");
            System.out.println("获取方式：https://lbs.amap.com/api/webservice/guide/api/weatherinfo");
            System.out.println("\n模拟天气数据展示：");
            printMockWeatherCard();
        } else {
            displayWeatherCard();
        }
    }
    
    /**
     * 打印模拟的天气卡片（用于演示）
     */
    private static void printMockWeatherCard() {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║         🌤️  杭州天气卡片  🌤️           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  📍 地区：%-24s ║\n", "浙江省 杭州市");
        System.out.printf("║  🌡️ 天气：%-24s ║\n", "⛅ 多云");
        System.out.printf("║  🌡️ 温度：%-24s ║\n", "25°C");
        System.out.printf("║  💨 风向：%-24s ║\n", "东南风");
        System.out.printf("║  💨 风力：%-24s ║\n", "3级");
        System.out.printf("║  💧 湿度：%-24s ║\n", "65%");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║  🕐 更新时间：%-20s ║\n", "2024-01-15 14:30:00");
        System.out.println("╚════════════════════════════════════════╝");
    }
}
