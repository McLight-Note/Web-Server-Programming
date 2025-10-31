import os
import csv
import requests
from flask import Flask, jsonify

# Simple Flask app that merges local CSV city data with live weather from OpenWeather

app = Flask(__name__)
API_KEY = "b07e8ddffaf4fc751a85d58c41058b7a" 

# Landing page
@app.get("/")
def index():
    return """<!DOCTYPE html>
<html>
<head>
    <title>City Weather</title>
</head>
<body>
    <h1>City Weather</h1>
    <p>Click <a href="/city-weather">here</a> to see weather for all cities.</p>
</body>
</html>"""

# Reads cities and populations from cities.csv
def read_cities(csv_path):
    cities = []
    with open(csv_path, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        for row in reader:
            city = (row.get("city") or "").strip()
            population_str = (row.get("population") or "0").replace(",", "").strip()
            try:
                population = int(population_str)
            except ValueError:
                population = None
            if city:
                cities.append({"city": city, "population": population})
    return cities

# Fetch weather data for a single city
def fetch_weather_for_city(city, api_key):
    base_url = "https://api.openweathermap.org/data/2.5/weather"
    params = {"q": city, "appid": api_key, "units": "metric"}
    resp = requests.get(base_url, params=params, timeout=10)
    if resp.status_code != 200:
        return {"error": True, "status": resp.status_code, "message": resp.text}
    data = resp.json()
    main = data.get("main") or {}
    weather_list = data.get("weather") or []
    weather_main = weather_list[0].get("main") if weather_list else None
    wind = data.get("wind") or {}
    return {
        "temp_c": main.get("temp"),
        "humidity": main.get("humidity"),
        "weather": weather_main,
        "wind_speed": wind.get("speed"),
        "country": ((data.get("sys") or {}).get("country")),
    }

# API endpoint that merges CSV population with live weather per city
@app.get("/city-weather")
def city_weather():
    api_key = API_KEY

    # Load local cities
    csv_path = os.path.join(os.path.dirname(__file__), "cities.csv")
    cities = read_cities(csv_path)
    results = []

    # Fetch weather for each city and combine
    for entry in cities:
        city = entry["city"]
        population = entry["population"]
        try:
            weather = fetch_weather_for_city(city, api_key)
        except requests.RequestException as e:
            weather = {"error": True, "message": str(e)}
        results.append({
            "city": city,
            "population": population,
            "weather": weather
        })
    return jsonify({"results": results})


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=6000, debug=True)