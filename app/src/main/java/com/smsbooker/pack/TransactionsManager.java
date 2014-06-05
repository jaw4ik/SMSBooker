package com.smsbooker.pack;

import android.content.Context;

import com.smsbooker.pack.models.Card;
import com.smsbooker.pack.models.CardPattern;
import com.smsbooker.pack.models.Message;
import com.smsbooker.pack.models.MessagePart;
import com.smsbooker.pack.models.Transaction;
import com.smsbooker.pack.repositories.CardPatternsRepository;
import com.smsbooker.pack.repositories.MessagesRepository;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Yuriy on 01.06.2014.
 */
public class TransactionsManager {

    public static ArrayList<Transaction> getTransactionsForCard(Context context, Card card){
        MessagesRepository messagesRepository = new MessagesRepository(context);

        ArrayList<Transaction> transactionsList = new ArrayList<Transaction>();

        for (CardPattern pattern : card.cardPatterns){
            ArrayList<Message> messages = messagesRepository.getMessagesByAddress(pattern.address);

            for (Message message : messages){
                Transaction transaction = getTransaction(pattern, message);
                if (transaction == null) { continue; }

                transactionsList.add(transaction);
            }
        }

        Collections.sort(transactionsList);

        return transactionsList;
    }

    public static Transaction getTransaction(Context context, String address, String messageBody){
        CardPatternsRepository cardPatternsRepository = new CardPatternsRepository(context);
        ArrayList<CardPattern> cardPatterns = cardPatternsRepository.getCardPatternsByAddress(address);

        if (cardPatterns.size() <= 0){
            return null;
        }

        Message message = new Message(messageBody, Calendar.getInstance().getTime().getTime());

        for (CardPattern pattern : cardPatterns){
            Transaction transaction = getTransaction(pattern, message);

            if (transaction != null){
                transaction.cardId = pattern.cardId;

                return transaction;
            }
        }

        return null;
    }

    private static Transaction getTransaction(CardPattern cardPattern, Message message){
        MessageParser messageParser = new MessageParser(message.messageBody);

        ArrayList<MessagePart> messageParts = messageParser.getMessageParts();
        if (!checkMessage(messageParts, cardPattern.checkWord)){
            return null;
        }

        String quantity = messageParser.getPatternValue(cardPattern.quantityValuePattern);
        if (quantity == null){
            return null;
        }

        String balance = messageParser.getPatternValue(cardPattern.balanceValuePattern);
        if (balance == null){
            return null;
        }

        Transaction transaction = new Transaction();

        try {
            transaction.value = Float.parseFloat(quantity.replace(",", "."));
            transaction.balance = Float.parseFloat(balance.replace(",", "."));
        } catch (Exception e){
            return null;
        }

        transaction.type = cardPattern.transactionType;
        transaction.createdOn = message.timestamp;
        transaction.message = message.messageBody;

        return transaction;
    }

    private static boolean checkMessage(ArrayList<MessagePart> messageParts, String checkword){
        for (MessagePart part : messageParts){
            if (part.value.equalsIgnoreCase(checkword)){
                return true;
            }
        }
        return false;
    }
}
