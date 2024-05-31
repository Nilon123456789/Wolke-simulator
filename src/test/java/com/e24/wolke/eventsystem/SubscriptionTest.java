package com.e24.wolke.eventsystem;

import java.util.ArrayList;
import java.util.function.Consumer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de la classe {@code Subscription}.
 *
 * @see Subscription
 * @author MeriBouisri
 */
public class SubscriptionTest {

  /** Test permettant de tester la methode {@code Subscription#equals(Object)} */
  @Test
  public void testEquals() {
    Subscription subObject1 = new Subscription(Subject.ON_TEST_PUBSUB_OBJECT);
    Subscription subObject2 = new Subscription(Subject.ON_TEST_PUBSUB_OBJECT);
    Subscription subInteger = new Subscription(Subject.ON_TEST_PUBSUB_INTEGER);

    // Check that subscriptions are equal if same subject
    Assert.assertEquals(subObject1, subObject2);

    // Check that subscriptions are not equal if not same subject
    Assert.assertNotEquals(subObject1, subInteger);

    // Check that subscriptions equal to corresponding subject
    Assert.assertEquals(subInteger, Subject.ON_TEST_PUBSUB_INTEGER);

    // Check that subscriptions not equal to non-corresponding subject
    Assert.assertNotEquals(subInteger, Subject.ON_TEST_PUBSUB_OBJECT);
  }

  /**
   * Test permettant de tester la methode {@code Subscription#subscribe(Consumer)} et {@code
   * Subscription#unsubscribe(Consumer)}
   */
  @Test
  public void testSubscribeUnsubscribe() {
    Subscription subObject = new Subscription(Subject.ON_TEST_PUBSUB_OBJECT);
    Consumer<Object> callback = o -> o.toString();

    Assert.assertTrue(subObject.subscribe(callback));
    Assert.assertTrue(subObject.unsubscribe(callback));
    Assert.assertFalse(subObject.unsubscribe(callback));
  }

  /** Test permettant de tester la methode {@code Subscription#invokeCallbacks(Object)}. */
  @Test
  public void testInvokeCallbacks() {
    Subscription subscription = new Subscription(Subject.ON_TEST_PUBSUB_OBJECT);

    int nbCallbacks = 10;
    ArrayList<Object> list = new ArrayList<Object>();

    for (int i = 0; i < nbCallbacks; i++) subscription.subscribe(n -> list.add(n));

    // Check state before calling callback
    Assert.assertEquals(0, list.size());

    // Invoke callbacks and check that it was successful
    Assert.assertTrue(subscription.invokeCallbacks(new Object()));

    // Check that callbacks wont be called with invalid message
    Assert.assertFalse(subscription.invokeCallbacks(null));

    // Check state after invoking callbacks
    Assert.assertEquals(nbCallbacks, list.size());
  }

  /** Test pour {@code Subscription#unsubscribeAll(java.util.Collection)} */
  @Test
  public void testUnsubscribeAll() {
    Subscription sub = new Subscription(Subject.ON_TEST_PUBSUB_OBJECT);

    int nbCb0 = 3;
    int nbCb1 = 2;

    ArrayList<Object> list0 = new ArrayList<Object>();
    ArrayList<Object> list1 = new ArrayList<Object>();
    ArrayList<Consumer<Object>> cbs = new ArrayList<Consumer<Object>>();

    for (int i = 0; i < nbCb0; i++) sub.subscribe(o -> list0.add(o));

    for (int i = 0; i < nbCb1; i++) {
      Consumer<Object> c = o -> list1.add(o);
      cbs.add(c);
      sub.subscribe(c);
    }

    Assert.assertTrue(list0.isEmpty());
    Assert.assertTrue(list1.isEmpty());

    sub.invokeCallbacks(new Object());

    Assert.assertEquals(nbCb0, list0.size());
    Assert.assertEquals(nbCb1, list1.size());

    sub.unsubscribeAll(cbs);

    sub.invokeCallbacks(new Object());
    Assert.assertEquals(nbCb0 * 2, list0.size());
    Assert.assertEquals(nbCb1, list1.size());
  }
}
