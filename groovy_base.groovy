// 快捷方式：输入 s o u t 输出 println
// 末尾可不加分号
println 18;
println 18
println "groovy"
// def定义变量，弱类型，会自动类型推断
def i = 18
println(i);
println i
def s = "groovy"
def t = 'groovy2'
println s
println t
// def定义集合
def list = ["a", 'b']
// 往集合中添加元素
list << 'c'
// 获取集合中的元素
println list.get(2)
// def定义map
def map = ["key1": "value1", "key2": "value2"]
// map中添加元素
map.key3 = "value3"
// 从map中获取元素
println map.get("key3")
// def定义闭包
def clo = {
    println "closure"
}
// def定义方法，入参Closure表示一个闭包
static def m1(Closure closure) {
    closure()
}
// 调用方法m1
m1(clo)
// def定义带参数闭包
def clo2 = {
    v -> println "hello ${v}"
}
// def定义方法m2接收带参数闭包
static def m2(Closure closure) {
    closure("world")
}
// 调用方法m2
m2(clo2)
