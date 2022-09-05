import { GameObject } from "./GameObject"
import { Snake } from "./Snake"
import { Wall } from "./Wall"

export class GameMap extends GameObject {
  // ctx 画布，parent 画布父元素
  constructor(ctx, parent, store) {
    super()
    this.ctx = ctx
    this.parent = parent
    this.store = store
    this.L = 0 // 每个格子的单位长度
    this.rows = 13
    this.cols = 14
    this.innerWallsCount = 30
    this.walls = []
    this.snakes = [
      new Snake({ id: 0, color: "#5280f0", r: this.rows - 2, c: 1 }, this),
      new Snake({ id: 1, color: "#fa5252", r: 1, c: this.cols - 2 }, this),
    ]
  }

  // 检查生成障碍物后，是否可以从左下角走到右上角

  createWalls() {
    const g = this.store.state.pk.gameMap
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if (g[r][c]) {
          this.walls.push(new Wall(r, c, this))
        }
      }
    }
  }

  addKeyMapListener() {
    if (this.store.state.record.is_record) {
      let k = 0
      const [snake0, snake1] = this.snakes
      const a_steps = this.store.state.record.a_steps
      const b_steps = this.store.state.record.b_steps
      const recordLoser = this.store.state.record.recordLoser
      const intervalId = setInterval(() => {
        if (k >= a_steps.length - 1) {
          if (recordLoser === "all" || recordLoser === "A") {
            snake0.status = "die"
          }
          if (recordLoser === "all" || recordLoser === "B") {
            snake1.status = "die"
          }
          clearInterval(intervalId)
        } else {
          snake0.setDirection(parseInt(a_steps[k]))
          snake1.setDirection(parseInt(b_steps[k]))
          k++
        }
      }, 300)
    } else {
      this.ctx.canvas.focus()
      this.ctx.canvas.addEventListener("keydown", (e) => {
        let d = -1
        if (e.key === "w") d = 0
        else if (e.key === "d") d = 1
        else if (e.key === "s") d = 2
        else if (e.key === "a") d = 3
        if (d >= 0) {
          this.store.state.pk.socket.send(
            JSON.stringify({
              event: "move",
              direction: d,
            })
          )
        }
      })
    }
  }

  start() {
    this.createWalls()
    this.addKeyMapListener()
  }

  updateSize() {
    // canvas的绘制长度是整数，所以将每个单位长度取整
    this.L = parseInt(
      Math.min(
        this.parent.clientWidth / this.cols,
        this.parent.clientHeight / this.rows
      )
    )
    this.ctx.canvas.width = this.L * this.cols
    this.ctx.canvas.height = this.L * this.rows
  }

  // 判断当前两位选手是否都已经准备好
  checkReady() {
    for (const snake of this.snakes) {
      if (snake.status !== "idle" || snake.direction === -1) return false
    }
    return true
  }

  // 两条蛇往下走一步/回合
  nextStep() {
    for (const snake of this.snakes) {
      snake.nextStep()
    }
  }

  update() {
    this.updateSize()
    if (this.checkReady()) {
      this.nextStep()
    }
    this.render()
  }

  render() {
    const color_even = "#b4dc5b",
      color_odd = "#abd652"
    for (let r = 0; r < this.rows; r++) {
      for (let c = 0; c < this.cols; c++) {
        if ((r + c) % 2 == 0) {
          this.ctx.fillStyle = color_even
        } else {
          this.ctx.fillStyle = color_odd
        }
        this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L)
      }
    }
  }
}
