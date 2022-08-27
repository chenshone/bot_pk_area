const GAME_OBJECTS = []

export class GameObject {
    constructor() {
        this.timeDelta = 0 // 这一帧距离上一帧的时间间隔
        this.hasCalledStarted = false
        GAME_OBJECTS.push(this)
    }
    // 创建时候执行一次
    start() {}

    // 每一帧执行一次，第一帧除外
    update() {}

    beforeDestroy() {}

    destroy() {
        this.beforeDestroy()
        for (let i in GAME_OBJECTS) {
            const item = GAME_OBJECTS[i]
            if (item === this) {
                GAME_OBJECTS.splice(i)
            }
        }
    }
}

// 每一帧渲染之前做一次
let lastTimestamp // 上一次执行时刻
const step = (timestamp) => {
    for (let obj of GAME_OBJECTS) {
        if (!obj.hasCalledStarted) {
            obj.hasCalledStarted = true
            obj.start()
        } else {
            obj.timeDelta = timestamp - lastTimestamp
            obj.update()
        }
    }
    lastTimestamp = timestamp
    requestAnimationFrame(step)
}
requestAnimationFrame(step)
