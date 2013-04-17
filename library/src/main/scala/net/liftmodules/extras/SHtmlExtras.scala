package net.liftmodules.extras

import scala.xml.{Elem, NodeSeq, Null, UnprefixedAttribute}

import net.liftweb._
import common._
import http._
import util.Helpers._

object SHtmlExtras {

  import SHtml.ElemAttr
  import S.{AFuncHolder, LFuncHolder, SFuncHolder, fmapFunc}

  private def selected(in: Boolean) = if (in) new UnprefixedAttribute("selected", "selected", Null) else Null

  private def secureOptions[T](options: Seq[(T, String, String)], default: Box[T],
                               onSubmit: T => Any): (Seq[(String, String, String)], Box[String], AFuncHolder) = {
    val secure = options.map {case (obj, txt, grp) => (obj, randomString(20), txt, grp)}
    val defaultNonce = default.flatMap(d => secure.find(_._1 == d).map(_._2))
    val nonces = secure.map {case (obj, nonce, txt, grp) => (nonce, txt, grp)}

    def process(nonce: String): Unit =
      secure.find(_._2 == nonce).map(x => onSubmit(x._1))
    (nonces, defaultNonce, SFuncHolder(process))
  }

  /**
   * Create a select box based on the list with a default value and the function
   * to be executed on form submission
   *
   * @param options -- a list of value and text pairs (value, text to display)
   * @param default -- the default value (or Empty if no default value)
   * @param onSubmit -- the function to execute on form submission
   */
  def selectObj[T](options: Seq[(T, String, String)], default: Box[T],
                   onSubmit: T => Any, attrs: ElemAttr*): Elem = {
    val (nonces, defaultNonce, secureOnSubmit) =
    secureOptions(options, default, onSubmit)

    select_*(nonces, defaultNonce, secureOnSubmit, attrs: _*)
  }

  /**
   * Create a select box based on the list with a default value and the function to be executed on
   * form submission
   *
   * @param opts -- the options.  A list of value and text pairs
   * @param deflt -- the default value (or Empty if no default value)
   * @param func -- the function to execute on form submission
   */
  def select_*(opts: Seq[(String, String, String)], deflt: Box[String],
               func: AFuncHolder, attrs: ElemAttr*): Elem = {
    val vals = opts.map(_._1)
    val testFunc = LFuncHolder(in => in.filter(v => vals.contains(v)) match {case Nil => false case xs => func(xs)}, func.owner)
    val optMap: Map[String, Seq[(String, String, String)]] = opts.groupBy(_._3)

    attrs.foldLeft(fmapFunc(testFunc)(fn => <select name={fn}>
      <option value=""></option>{
      optMap.keySet.toSeq.sortWith(_ < _).flatMap { k =>
        <optgroup label={k}>
          {
          optMap(k).flatMap { case (value, text, grp) =>
            <option value={value}>{text}</option> % selected(deflt.exists(_ == value))
          }
          }
        </optgroup>
      }
    }</select>))(_ % _)
  }
}
