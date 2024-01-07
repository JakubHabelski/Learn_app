package com.learn.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface AddFlashCardSetInterface extends JpaRepository<FlashCardSet,Long> {
}
