import { Cell } from "./Cell"
import { GameObject } from "./GameObject"

export class Snake extends GameObject {
    constructor(info, gameMap) {
        super()

        this.id = info.id
        this.color = info.color
        this.gameMap = gameMap
        // 蛇的身体，cells[0]表示蛇头
        this.cells = [new Cell(info.r, info.c)]
        // 每秒走5个格子
        this.speed = 5
        this.direction = -1 // -1表示没有指令， 0 1 2 3表示上右下左
        this.status = "idle" // idle表示静止，move表示移动，die表示死亡
        this.nextCell = null // 下一步目的地

        // 四个方向的偏移量
        this.dr = [-1, 0, 1, 0]
        this.dc = [0, 1, 0, -1]

        this.step = 0 // 当前回合数 也就是走的步数
        this.eps = 1e-2 // 允许的误差

        // 左下角蛇初始方向朝上，右上角蛇初始方向朝下
        this.eyeDirection = 0
        if (this.id === 1) this.eyeDirection = 2
        // 蛇头眼睛距离中心点的偏移量
        this.eyeDx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1],
        ]
        this.eyeDy = [
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1],
        ]
    }

    start() {}

    setDirection(d) {
        this.direction = d
    }

    checkTailIncreasing() {
        if (this.step <= 10) return true
        if (this.step % 3 === 1) return true
        return false
    }

    nextStep() {
        const d = this.direction
        this.eyeDirection = d
        this.nextCell = new Cell(
            this.cells[0].r + this.dr[d],
            this.cells[0].c + this.dc[d]
        )
        this.direction = -1 //清空操作
        this.status = "move"
        this.step++

        const len = this.cells.length
        for (let i = len; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]))
        }
    }

    updateMove() {
        const dx = this.nextCell.x - this.cells[0].x
        const dy = this.nextCell.y - this.cells[0].y
        const distance = Math.sqrt(dx * dx + dy * dy)

        if (distance < this.eps) {
            // 一步走完了
            // 添加新蛇头
            this.cells[0] = this.nextCell
            this.nextCell = null
            this.status = "idle" // 停下来

            // 如果蛇身没有变长，需要将原尾巴pop
            if (!this.checkTailIncreasing()) this.cells.pop()
        } else {
            const moveDistance = (this.speed * this.timeDelta) / 1000 // 每两帧之间的距离
            this.cells[0].x += (moveDistance * dx) / distance
            this.cells[0].y += (moveDistance * dy) / distance

            // 蛇尾不变长的话，蛇尾就需要移动
            if (!this.checkTailIncreasing()) {
                const k = this.cells.length
                const tail = this.cells[k - 1],
                    tailTarget = this.cells[k - 2]
                const tailDx = tailTarget.x - tail.x
                const tailDy = tailTarget.y - tail.y
                tail.x += (moveDistance * tailDx) / distance
                tail.y += (moveDistance * tailDy) / distance
            }
        }
    }

    update() {
        if (this.status === "move") this.updateMove()
        this.render()
    }

    render() {
        const L = this.gameMap.L
        const ctx = this.gameMap.ctx

        ctx.fillStyle = this.color
        if (this.status === "die") ctx.fillStyle = "white"
        for (const cell of this.cells) {
            ctx.beginPath()
            ctx.arc(cell.x * L, cell.y * L, (L / 2) * 0.8, 0, Math.PI * 2)
            ctx.fill()
        }

        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1],
                b = this.cells[i]
            if (
                Math.abs(a.x - b.x) < this.eps &&
                Math.abs(a.y - b.y) < this.eps
            )
                continue
            // 竖直方向
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect(
                    (a.x - 0.4) * L,
                    Math.min(a.y, b.y) * L,
                    L * 0.8,
                    Math.abs(a.y - b.y) * L
                )
            } else {
                ctx.fillRect(
                    Math.min(a.x, b.x) * L,
                    (a.y - 0.4) * L,
                    Math.abs(a.x - b.x) * L,
                    L * 0.8
                )
            }
        }
        ctx.fillStyle = "black"
        for (let i = 0; i < 2; i++) {
            const eyeX =
                (this.cells[0].x + this.eyeDx[this.eyeDirection][i] * 0.15) * L
            const eyeY =
                (this.cells[0].y + this.eyeDy[this.eyeDirection][i] * 0.15) * L
            ctx.beginPath()
            ctx.arc(eyeX, eyeY, L * 0.05, 0, Math.PI * 2)
            ctx.fill()
        }
    }
}
