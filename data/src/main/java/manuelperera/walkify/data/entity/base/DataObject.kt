package manuelperera.walkify.data.entity.base

interface DataObject<out DomainObject : Any?> {

    fun toDomain(): DomainObject

}