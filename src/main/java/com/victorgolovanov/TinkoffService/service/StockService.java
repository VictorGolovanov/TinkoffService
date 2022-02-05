package com.victorgolovanov.TinkoffService.service;

import com.victorgolovanov.TinkoffService.model.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
