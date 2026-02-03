package com.teobaranga.monica.genders.data

import com.teobaranga.monica.genders.domain.Gender

val genderMale = GenderEntity(
    genderId = 1,
    name = "Male",
)

val genderFemale = GenderEntity(
    genderId = 2,
    name = "Female",
)

fun GenderEntity.toDomain() = Gender(
    id = genderId,
    name = name,
)
