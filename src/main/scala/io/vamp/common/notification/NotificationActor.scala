package io.vamp.common.notification

import akka.actor.{AbstractLoggingActor, Actor, Props}
import io.vamp.common.akka.ActorExecutionContextProvider
import io.vamp.common.pulse.PulseClientProvider
import io.vamp.pulse.api.Event

object LoggingNotificationActor {
  def props: Props = Props[LoggingNotificationActor]
}

object DefaultPulseNotificationActor {
  def props(url: String): Props = Props(new DefaultPulseNotificationActor(url))
}

case class Error(notification: Notification, message: String)

case class Info(notification: Notification, message: String)

trait NotificationActor  {
  this: Actor =>
  override def receive: Receive = {
    case Error(notification, message) => error(notification, message)
    case Info(notification, message)  =>  info(notification, message)
  }

  def error(notification: Notification, message: String)

  def info(notification: Notification, message: String)
}

class LoggingNotificationActor extends AbstractLoggingActor with NotificationActor {
  override def error(notification: Notification, message: String): Unit = {
    log.error(message)
  }

  override def info(notification: Notification, message: String): Unit = {
    log.info(message)
  }
}

class DefaultPulseNotificationActor(override protected val url: String) extends AbstractPulseNotificationActor(url) with DefaultTagResolverProvider with DefaultNotificationEventFormatter {

}

abstract class AbstractPulseNotificationActor(override protected val url: String) extends AbstractLoggingActor with NotificationActor with TagResolverProvider with PulseClientProvider with PulseNotificationEventFormatter with ActorExecutionContextProvider  {
  override def error(notification: Notification, message: String): Unit = {
    client.sendEvent(formatNotification(notification, List("notification", "error")))
  }

  override def info(notification: Notification, message: String): Unit = {
    client.sendEvent(formatNotification(notification, List("notification", "info")))
  }
}

