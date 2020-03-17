package manuelperera.walkify.data.entity.base

interface ResponseObject<out DomainObject : Any?> {

    fun toDomain(): DomainObject

}