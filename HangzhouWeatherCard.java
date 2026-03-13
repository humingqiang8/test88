import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 * 杭州天气查询卡片
 * 使用 OpenWeatherMap API 查询杭州实时天气
 */
public class HangzhouWeatherCard {
    
    // 替换为您的 OpenWeatherMap API Key
    private static final String API_KEY = "YOUR_API_KEY_HERE";
    private static final String CITY = "Hangzhou";
    private static final String COUNTRY_CODE = "CN";
    private static final String UNITS = "metric"; // 摄氏度
    
    public static void main(String[] args) {
        try {
            HangzhouWeatherCard weatherCard = new HangzhouWeatherCard();
            weatherCard.displayWeatherCard();
        } catch (Exception e) {
            System.err.println("获取天气信息失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 显示天气卡片
     */
    public void displayWeatherCard() throws Exception {
        WeatherData weather = getWeatherData();
        
        if (weather != null) {
            printWeatherCard(weather);
        } else {
            System.out.println("无法获取天气数据");
        }
    }
    
    /**
     * 从 API 获取天气数据
     */
    public WeatherData getWeatherData() throws Exception {
        String urlString = String.format(
            "https://api.openweathermap.org/data/2.5/weather?q=%s,%s&appid=%s&units=%s&lang=zh_cn",
            CITY, COUNTRY_CODE, API_KEY, UNITS
        );
        
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            return parseWeatherData(response.toString());
        } else {
            throw new Exception("API 请求失败，响应码: " + responseCode);
        }
    }
    
    /**
     * 解析 JSON 天气数据
     */
    private WeatherData parseWeatherData(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            
            // 提取主要天气信息
            JSONObject main = json.getJSONObject("main");
            JSONArray weatherArray = json.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            JSONObject wind = json.getJSONObject("wind");
            
            WeatherData data = new WeatherData();
            data.city = json.getString("name");
            data.country = json.getJSONObject("sys").getString("country");
            data.temperature = main.getDouble("temp");
            data.feelsLike = main.getDouble("feels_like");
            data.humidity = main.getInt("humidity");
            data.pressure = main.getInt("pressure");
            data.description = weather.getString("description");
            data.icon = weather.getString("icon");
            data.windSpeed = wind.getDouble("speed");
            
            return data;
        } catch (Exception e) {
            System.err.println("解析天气数据失败: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * 打印格式化的天气卡片
     */
    private void printWeatherCard(WeatherData weather) {
        String separator = "════════════════════════════════════";
        String icon = getWeatherIcon(weather.icon);
        
        System.out.println();
        System.out.println(separator);
        System.out.println("           🌤️  杭州天气卡片  🌤️");
        System.out.println(separator);
        System.out.println();
        System.out.println("  📍 城市: " + weather.city + ", " + weather.country);
        System.out.println("  " + icon + "  天气: " + weather.description);
        System.out.println();
        System.out.println("  🌡️  温度: " + String.format("%.1f", weather.temperature) + "°C");
        System.out.println("  💭 体感温度: " + String.format("%.1f", weather.feelsLike) + "°C");
        System.out.println();
        System.out.println("  💧 湿度: " + weather.humidity + "%");
        System.out.println("  🔵 气压: " + weather.pressure + " hPa");
        System.out.println("  💨 风速: " + String.format("%.1f", weather.windSpeed) + " m/s");
        System.out.println();
        System.out.println(separator);
        System.out.println("  更新时间: " + new java.util.Date());
        System.out.println(separator);
        System.out.println();
    }
    
    /**
     * 根据天气图标代码返回对应的 emoji
     */
    private String getWeatherIcon(String iconCode) {
        switch (iconCode) {
            case "01d": return "☀️";
            case "01n": return "🌙";
            case "02d": return "⛅";
            case "02n": return "☁️";
            case "03d": 
            case "03n": return "☁️";
            case "04d": 
            case "04n": return "☁️";
            case "09d": 
            case "09n": return "🌧️";
            case "10d": return "🌦️";
            case "10n": return "🌧️";
            case "11d": 
            case "11n": return "⛈️";
            case "13d": 
            case "13n": return "❄️";
            case "50d": 
            case "50n": return "🌫️";
            default: return "🌤️";
        }
    }
    
    /**
     * 天气数据内部类
     */
    static class WeatherData {
        String city;
        String country;
        double temperature;
        double feelsLike;
        int humidity;
        int pressure;
        String description;
        String icon;
        double windSpeed;
    }
}
