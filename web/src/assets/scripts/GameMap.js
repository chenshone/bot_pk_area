import { GameObject } from "./GameObject"
import { Snake } from "./Snake"
import { Wall } from "./Wall"

export class GameMap extends GameObject {
    // ctx 画布，parent 画布父元素
    constructor(ctx, parent) {
        super()
        this.ctx = ctx
        this.parent = parent
        this.L = 0 // 每个格子的单位长度
        this.rows = 13
        this.cols = 14
        this.innerWallsCount = 30
        this.walls = []
        this.snakes = [
            new Snake(
                { id: 0, color: "#5280f0", r: this.rows - 2, c: 1 },
                this
            ),
            new Snake(
                { id: 1, color: "#fa5252", r: 1, c: this.cols - 2 },
                this
            ),
        ]
    }

    // 检查生成障碍物后，是否可以从左下角走到右上角
    // dfs blood-fill
    checkConnectivity(g, sx, sy, tx, ty) {
        if (sx == tx && sy == ty) return true
        g[sx][sy] = true

        let dx = [-1, 0, 1, 0],
            dy = [0, 1, 0, -1]
        for (let i = 0; i < 4; i++) {
            let x = sx + dx[i],
                y = sy + dy[i]
            if (!g[x][y] && this.checkConnectivity(g, x, y, tx, ty)) return true
        }
        return false
    }

    createWalls() {
        const g = []
        for (let r = 0; r < this.rows; r++) {
            g[r] = []
            for (let c = 0; c < this.cols; c++) {
                g[r][c] = false
            }
        }
        // 四周围墙
        for (let r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = true
        }
        for (let c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = true
        }

        // 随机生成图内障碍物
        for (let i = 0; i < this.innerWallsCount / 2; i++) {
            // 放置随机数冲突，随机1000次
            for (let j = 0; j < 1000; j++) {
                let r = parseInt(Math.random() * this.rows)
                let c = parseInt(Math.random() * this.cols)
                // 中心对称
                if (g[r][c] || g[this.rows - 1 - r][this.cols - 1 - c]) continue
                if (
                    (r == this.rows - 2 && c == 1) ||
                    (r == 1 && c == this.cols - 2)
                )
                    continue
                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = true
                break
            }
        }
        const gBackup = JSON.parse(JSON.stringify(g))
        if (
            !this.checkConnectivity(gBackup, this.rows - 2, 1, 1, this.cols - 2)
        )
            return false

        for (let r = 0; r < this.rows; r++) {
            for (let c = 0; c < this.cols; c++) {
                if (g[r][c]) {
                    this.walls.push(new Wall(r, c, this))
                }
            }
        }
        return true
    }

    addKeyMapListener() {
        this.ctx.canvas.focus()

        const [snake0, snake1] = this.snakes
        this.ctx.canvas.addEventListener("keydown", (e) => {
            if (e.key === "w") snake0.setDirection(0)
            else if (e.key === "d") snake0.setDirection(1)
            else if (e.key === "s") snake0.setDirection(2)
            else if (e.key === "a") snake0.setDirection(3)
            else if (e.key === "ArrowUp") snake1.setDirection(0)
            else if (e.key === "ArrowRight") snake1.setDirection(1)
            else if (e.key === "ArrowDown") snake1.setDirection(2)
            else if (e.key === "ArrowLeft") snake1.setDirection(3)
        })
    }

    start() {
        for (let i = 0; i < 1000; i++) {
            if (this.createWalls()) break
        }
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

    // 检查蛇是否撞到身体和障碍物
    checkValid(cell) {
        for (const wall of this.walls)
            if (cell.r === wall.r && cell.c === wall.c) return false

        for (const snake of this.snakes) {
            let k = snake.cells.length
            // 如果蛇身不增加，那么蛇尾就不用判断，因为下一步蛇尾可以往前移动
            if (!snake.checkTailIncreasing()) {
                k--
            }
            for (let i = 0; i < k; i++)
                if (cell.r === snake.cells[i].r && cell.c === snake.cells[i].c)
                    return false
        }
        return true
    }

    checkDie() {
        for (const snake of this.snakes) {
            if (!this.checkValid(snake.nextCell)) snake.status = "die"
        }
    }

    update() {
        this.updateSize()
        if (this.checkReady()) {
            this.nextStep()
            this.checkDie()
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
