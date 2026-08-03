importPackage(java.lang.invoke)

const type = Class.forName("env.EnvAlloc", true, Vars.mods.mainLoader())
const lookup = MethodHandles.lookup().in(type)
const create = lookup.findStatic(type, "create", MethodType.methodType(Integer.TYPE, java.lang.String))

exports.create = function(name){
    return create.invokeWithArguments([name])
}