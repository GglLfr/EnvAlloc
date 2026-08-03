importPackage(java.lang.reflect)

const type = Class.forName("env.EnvAlloc", true, Vars.mods.mainLoader())
const create = type.getMethod("create", [java.lang.String])

exports.create = function(name){
    return create.invoke(null, name)
}
