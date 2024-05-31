package com.e24.wolke.eventsystem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de la classe {@code EventBroker}.
 *
 * @see EventBroker
 * @author MeriBouisri
 */
public class EventBrokerTest {

  // TODO : testSubscribe
  // TODO : testUnsubscribe
  // TODO : testPublish

  /** Test permettant de tester la methode {@code EventBroker#get(Subject)}. */
  @Test
  public void testGet() {
    EventBroker broker = new EventBroker();
    Subject subject = Subject.ON_TEST_PUBSUB_OBJECT;
    Subscription subscription = new Subscription(subject);

    try {
      Method privGet = EventBroker.class.getDeclaredMethod("get", Subject.class);
      Method privAdd = EventBroker.class.getDeclaredMethod("add", Subject.class);
      privGet.setAccessible(true);
      privAdd.setAccessible(true);

      // Try to get non-present subject
      Subscription retGet = (Subscription) privGet.invoke(broker, subject);
      Assert.assertNull(retGet);

      // Add the subject and check that it has been added successfully
      Boolean retAdd = (Boolean) privAdd.invoke(broker, subject);
      Assert.assertTrue(retAdd);

      retGet = (Subscription) privGet.invoke(broker, subject);
      Assert.assertEquals(subscription, retGet);

    } catch (Exception e) {
      Assert.fail();
    }
  }

  /** Test permettant de tester la methode {@code EventBroker#contains(Subject)}. */
  @Test
  public void testContains() {
    EventBroker broker = new EventBroker();
    Subject subject = Subject.ON_TEST_PUBSUB_OBJECT;

    try {
      Method privContains = EventBroker.class.getDeclaredMethod("contains", Subject.class);
      Method privAdd = EventBroker.class.getDeclaredMethod("add", Subject.class);
      privContains.setAccessible(true);
      privAdd.setAccessible(true);

      // Check that it doesnt contain subject yet
      Boolean retContains = (Boolean) privContains.invoke(broker, subject);
      Assert.assertFalse(retContains);

      // Add the subject and check that it has been added
      Boolean retAdd = (Boolean) privAdd.invoke(broker, subject);
      Assert.assertTrue(retAdd);

      // Check that it does contain subject after adding
      retContains = (Boolean) privContains.invoke(broker, subject);
      Assert.assertTrue(retContains);

    } catch (Exception e) {
      Assert.fail();
    }
  }

  /** Test permettant de tester la methode {@code EventBroker#add(Subject)}. */
  @Test
  public void testAdd() {
    EventBroker broker = new EventBroker();
    Subject subject = Subject.ON_TEST_PUBSUB_INTEGER;

    try {
      Method privAdd = EventBroker.class.getDeclaredMethod("add", Subject.class);
      privAdd.setAccessible(true);

      // Add the subject and check that it has been added successfully
      Boolean retAdd = (Boolean) privAdd.invoke(broker, subject);
      Assert.assertTrue(retAdd);

      // Check that subject cant be added if already present
      retAdd = (Boolean) privAdd.invoke(broker, subject);
      Assert.assertFalse(retAdd);

    } catch (Exception e) {
      Assert.fail();
    }
  }

  /** test pour {@code EventBroker#unsubscribeAllWithID(int)} */
  @Test
  public void testUnsubscribeAllWithID() {
    EventBroker broker = new EventBroker();

    ArrayList<Object> list0 = new ArrayList<Object>();
    ArrayList<Object> list1 = new ArrayList<Object>();

    Consumer<Object> cbObj0 = o -> list0.add(o);
    Consumer<Object> cbObj1 = o -> list1.add(o);

    int id0 = hashCode();
    int id1 = hashCode() - 1;

    broker.subscribeWithID(Subject.ON_TEST_PUBSUB_OBJECT, cbObj0, id0);
    broker.subscribeWithID(Subject.ON_TEST_PUBSUB_INTEGER, cbObj0, id0);
    broker.subscribeWithID(Subject.ON_TEST_PUBSUB_OBJECT, cbObj1, id1);
    broker.subscribeWithID(Subject.ON_TEST_PUBSUB_INTEGER, cbObj1, id1);

    Assert.assertTrue(list0.isEmpty());
    Assert.assertTrue(list1.isEmpty());

    broker.publish(Subject.ON_TEST_PUBSUB_INTEGER, 1);
    broker.publish(Subject.ON_TEST_PUBSUB_OBJECT, 1);
    Assert.assertEquals(2, list0.size());
    Assert.assertEquals(2, list1.size());

    broker.unsubscribeAllWithID(id0);

    broker.publish(Subject.ON_TEST_PUBSUB_INTEGER, 1);
    broker.publish(Subject.ON_TEST_PUBSUB_OBJECT, 1);

    Assert.assertEquals(2, list0.size());
    Assert.assertEquals(4, list1.size());
  }
}
