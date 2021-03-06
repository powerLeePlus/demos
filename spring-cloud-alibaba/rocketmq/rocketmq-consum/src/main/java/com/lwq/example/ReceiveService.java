package com.lwq.example;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author lwq
 * @date 2021/6/11 0011
 */
@Service
public class ReceiveService {

	@StreamListener("input1")
	public void receiveInput1(String receiveMsg) {
		System.out.println("input1 receive: " + receiveMsg);
	}

	@StreamListener("input2")
	public void receiveInput2(String receiveMsg) {
		System.out.println("input2 receive: " + receiveMsg);
	}

	@StreamListener("input3")
	public void receiveInput3(@Payload Foo foo) {
		System.out.println("input3 receive: " + foo);
	}

	@StreamListener("input4")
	public void receiveTransactionalMsg(String transactionalMsg) {
		System.out.println("input4 receive transaction msg: " + transactionalMsg);
	}
}
