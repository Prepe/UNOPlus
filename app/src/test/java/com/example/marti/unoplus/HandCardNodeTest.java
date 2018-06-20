package com.example.marti.unoplus;

import com.example.marti.unoplus.cards.Card;
import com.example.marti.unoplus.cards.HandCardNode;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by marti on 20.06.2018.
 */

public class HandCardNodeTest {
    Card dummyCard = new Card(Card.colors.RED, Card.values.ZERO);
    HandCardNode node;

    @Before
    public void setUp() {
        node = new HandCardNode(0, dummyCard,null,null);
    }

    @After
    public void cleanUp() {
        node = null;
    }

    @Test
    public void initHandCardNodeTest() {
        Assert.assertNotNull(node);
    }

    @Test
    public void nodeIndexTest() {
        Assert.assertEquals(0,node.getIndex());
    }

    @Test
    public void nodeCardTest() {
        Assert.assertNotNull(node.getCard());
    }

    @Test
    public void nodePointerTest() {
        Assert.assertNull(node.getNext());
        Assert.assertNull(node.getPrev());
    }

    @Test
    public void addNextNodeTest() {
        HandCardNode temp = new HandCardNode(1, dummyCard,null,node);

        Assert.assertNotNull(node.getNext());
        Assert.assertNotNull(temp.getPrev());
    }

    @Test
    public void addPrevNodeTest() {
        HandCardNode temp = new HandCardNode(0,dummyCard,node,null);

        Assert.assertNotNull(node.getPrev());
        Assert.assertNotNull(temp.getNext());
    }

    @Test
    public void fixIndexTest() {
        new HandCardNode(0,dummyCard,node,null);

        Assert.assertEquals(1,node.getIndex());
    }

    @Test
    public void removePointerTest() {
        new HandCardNode(0,dummyCard,node,null);
        new HandCardNode(2,dummyCard,null,node);
        node.removePointer();

        Assert.assertNull(node.getNext());
        Assert.assertNull(node.getPrev());
    }
}
