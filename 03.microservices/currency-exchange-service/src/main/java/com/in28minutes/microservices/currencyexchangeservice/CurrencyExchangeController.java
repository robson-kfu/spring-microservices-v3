package com.in28minutes.microservices.currencyexchangeservice;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    private final CurrencyExchangeRepository repository;

	private final String serverPort;

    public CurrencyExchangeController(CurrencyExchangeRepository repository,
									  @Value("${server.port}") String pServerPort) {
        this.repository = repository;
		this.serverPort = pServerPort;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,
                                                  @PathVariable String to) {

        logger.info("retrieveExchangeValue called with {} to {}", from, to);

        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);

        if (currencyExchange == null) {
            throw new EntityNotFoundException("Unable to Find currency exchange for " + from + " to " + to);
        }

        currencyExchange.setEnvironment(serverPort);

        return currencyExchange;

    }

}
