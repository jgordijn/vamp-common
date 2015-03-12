package io.magnetic.vamp_common.notification

import akka.actor.{Actor, AbstractLoggingActor, Props}
import io.magnetic.vamp_common.pulse.PulseClientProvider
import io.magnetic.vamp_common.pulse.api.Event

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

class DefaultPulseNotificationActor(override protected val url: String) extends AbstractPulseNotificationActor(url) with DefaultTagResolverProvider {

}

abstract class AbstractPulseNotificationActor(override protected val url: String) extends Actor with NotificationActor with TagResolverProvider with PulseClientProvider {
  override def error(notification: Notification, message: String): Unit = {
    client.sendEvent(
      Event(resolveTags(notification,List("info", "notification" )),
        Map("object" -> Map( notification.getClass.getCanonicalName -> notification))
      )
    )
  }

  override def info(notification: Notification, message: String): Unit = {
    client.sendEvent(
      Event(resolveTags(notification,List("error", "notification" )),
        Map("object" -> Map( notification.getClass.getCanonicalName -> notification))
      )
    )
  }
}

