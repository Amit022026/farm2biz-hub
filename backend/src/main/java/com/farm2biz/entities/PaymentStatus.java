package com.farm2biz.entities;

//The life cycle of a Payment - separate from OrderStatus. An Order can be
//ACCEPTED by a farmer while its Payment is still PENDING - these two
//statuses track two genuinely different things (has the farmer agreed
//to fulfill it? vs has money actually changed hands?)
public enum PaymentStatus {
	PENDING,
	SUCCESS,
	FAILED,
	REFUNDED
}
