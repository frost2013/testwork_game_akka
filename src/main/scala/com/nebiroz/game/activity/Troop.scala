package com.nebiroz.game.activity

import com.nebiroz.game.activity.army.Pawn
import com.nebiroz.game.activity.exceptions.NoMoreInArmyException
import com.nebiroz.game.activity.race.Race

import scala.util.Random

/**
  * Класс 1 отряда с воинами
  *
  * @param race - раса отряда
  */
class Troop(val name: String, val race: Race) {
  // главный констуктор. надо создать отряд
  private val pawns: List[Pawn] = makeDefaultArmy()

  /**
    * Создаем отряд по-умолчанию
    *  - 1 маг
    *  - 3 лучника
    *  - 4 бойца
    *
    * @return
    */
  def makeDefaultArmy(): List[Pawn] = {
    List(
      race.createMag("Маг 1"),

      race.createArcher("Лучник 1"),
      race.createArcher("Лучник 2"),
      race.createArcher("Лучник 3"),

      race.createFighter("Воин 1"),
      race.createFighter("Воин 2"),
      race.createFighter("Воин 3"),
      race.createFighter("Воин 4")
    )
  }

  /**
    * Возвращаем случайного воина из отряда
    *
    * @return - случайный воин
    */
  @throws(classOf[NoMoreInArmyException])
  def warrior(): Pawn = {
    val localTroop = pawns filter((pawn: Pawn) => pawn.isAlive) collect {
      case p: Pawn => p
    }
    if (localTroop.isEmpty) {
      null
    }
    else{
      localTroop(Random.nextInt(localTroop.size))
    }
  }

  /**
    * Метод возвращает следующего воина в отряде.
    * Если есть живые и те, кто могут ходить, то выбираем.
    * Выбираем сначала тех, кто проапргреден, и выбираем случайного воина из них.
    * Выбираем затем из остальных.
    *
    * @return
    */
  def nextWarrior(): Pawn = {
    // выбираем тех, кто с повышенным уровнем урона
    val morePower = pawns filter((pawn: Pawn) => (!pawn.isPlayed) && pawn.power() > 1.0) collect {
      case p: Pawn => p
    }
    // если такие есть - то возвращаем случйного воина из выбранных
    if (morePower.nonEmpty) {
      morePower(Random.nextInt(morePower.size))
    }
    // иначе выбираем из обычных воинов
    else {
      // выбираем кто живой и кто может походить
      val whoCan = pawns filter((pawn: Pawn) => (!pawn.isPlayed) && pawn.isAlive) collect {
        case p: Pawn => p
      }
      // возвращаем случайного воина
      if (whoCan.nonEmpty) {
        whoCan(Random.nextInt(whoCan.size))
      }
      else {
        null
      }
    }
  }

  /**
    * Проверяем есть ли живые в отряде
    *
    * @return - есть или нет
    */
  def isMoreAlive: Boolean = pawns.count((p: Pawn) => p.isAlive) > 0

  /**
    * Метод начинает новый раунд.
    * Если воин жив - то ему сбрасывается флаг ходьбы
    * Если воин мертв - то флаг наоборот, взводим, чтобы он не мог ходить
    *
    */
  def newRound(): Unit = {
    pawns foreach((p: Pawn) => {
      if (p.isAlive) {
        p.turnPlayOff()
      }
      else {
        p.turnPlayOn()
      }
    })
  }

  /**
    * Проверяем есть ли живые в отряде
    *
    * @return - есть или нет
    */
  def isMorePlayed: Boolean = pawns.count((p: Pawn) => !p.isPlayed) > 0

  /**
    * Вернуть количество живых в отряде
    *
    * @return - количество живых в отряде
    */
  def getCountOfAlive: Int = pawns.count((p: Pawn) => p.isAlive)

  /**
    * Возращаем статус по отряду
    *
    * @return - статус отряда
    */
  def status(): String = {
    val status: StringBuilder = new StringBuilder

    pawns foreach((p: Pawn) => {
      status append f"${p.health()}%10s"
      status append " | "
    })

    status toString()
  }

}
