# ForexProxy
A web service that acts as a proxy to the real foreign exchange service, `one-frame`.

## Running the service
### Using docker
```bash
docker-compose up
```

### Using Maven wrapper
From the project root run the following command
```bash
./mvnw spring-boot:run
```

In a new terminal window/tab, run `one-frame` using docker
```bash
docker run -p 8085:8080 paidyinc/one-frame
```

## Usage
### GET /rates
Get the latest currency rates.

#### Resource URL
```
http://localhost:8080/rates?pair={currency_pair_0}&pair={currency_pair_1}&...pair={currency_pair_n}
```

#### Parameters
- `pair` - Required query parameter that is the concatenation of two different currency codes, e.g. USDJPY. One or more pairs per request are allowed.

#### Example request
Using a browser or any REST client.
```
http://localhost:8080/rates?pair=USDJPY
```

#### Example response
```json
[
  {
    "from": "USD",
    "to": "JPY",
    "bid": 0.5098366425706436,
    "ask": 0.13104034760723204,
    "price": 0.32043849508893785,
    "time_stamp": "2022-09-10T06:31:01.002Z"
  }
]
```

## Limitations
`one-frame` can only accept 1000 requests per day. To alleviate this issue, `ForexProxy` will cache each requested currency pair. When a 429 (Too Many Requests) is returned by one-frame or any error for that matter, ForexProxy will try to get that currency pair from the cache.

This however is not foolproof. If a currency pair is requested and one-frame returns an error and the pair does not exist in cache, then an error will be returned to the user.

To make ForexProxy more resilient, there needs to be a background worker that would build all the combinations of all currency pairs and store them in cache.  
However due to time, this is not implemented.

## Tests
To run the tests, from the project root run the following command
```bash
./mvnw test
```
