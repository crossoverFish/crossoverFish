package com.tyu.service.impl;

import com.tyu.service.IEat;
import com.tyu.service.IDrink;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService implements IEat, IDrink {
    @Override
    public void eat(String food) {
      log.info("eat {}",food);
    }

    @Override
    public void drink(String drinks) {
        log.info("drink {}",drinks);
    }
}
