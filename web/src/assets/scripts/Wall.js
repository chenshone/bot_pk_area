import { GameObject } from "./GameObject"

export class Wall extends GameObject {
    // r c 坐标
    constructor(r, c, gameMap) {
        super()
        this.r = r
        this.c = c
        this.gameMap = gameMap
        this.color = "#bc7d2b"
    }

    update() {
        this.render()
    }

    render() {
        const L = this.gameMap.L
        const ctx = this.gameMap.ctx

        ctx.fillStyle = this.color
        ctx.fillRect(this.c * L, this.r * L, L, L)
    }
}
