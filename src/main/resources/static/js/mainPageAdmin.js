$(document).ready(async function () {
    async function submit() {
        const response = await fetch('http://localhost:8080/mainPage/allUsers')
        const result = await response.json();
        let roles = ""

        $.each(result, async function (index, value) {
            $('.table #users_table').append("<tr>")
            $.each(value.userData, function (index, value1) {
                $('.table #users_table').append("<td>" + value1 + "</td>")
            })
            $.each(value.roles, function (index, value2) {
                roles += value2 + "<br>"
            })
            $('.table #users_table').append("<td>" + roles + "</td>")
            roles = ""
            $('.table #users_table').append(
                $('<button/>', {
                    class: 'btn btn-primary',
                    text: 'Edit',
                    id: 'btn_edit',
                    click: async function (event) {
                        roles = ""
                        const href = await fetch('http://localhost:8080/mainPage/getUserById/' + value.userData[0])
                        const hrefJson = await href.json()
                        console.log(hrefJson)
                        event.preventDefault();
                        $('.myForm #userId').val(hrefJson.userId)
                        console.log(hrefJson.userId)
                        $('.myForm #userName').val(hrefJson.userName);
                        $('.myForm #userAge').val(hrefJson.userAge);

                        $.each(hrefJson.userRoles, function (index, value) {
                            if (value === "ROLE_ADMIN") {
                                $('.selDiv option[value="2"]').prop('selected', true);
                            }
                            if (value === "ROLE_USER") {
                                $('.selDiv option[value="1"]').prop('selected', true);
                            }
                        })


                        $('#exampleModal').modal();
                    }
                })
            )
            $('.table #users_table').append(
                $('<button/>', {
                    class: 'btn btn-primary',
                    text: 'Delete',
                    id: 'btn_delete',
                    click:function (event) {
                        $.ajax({
                            method: 'DELETE',
                            url: '/mainPage/' + value.userData[0],
                            headers: {'Content-type': 'application/json; charset=utf-8'},
                            success:function (response){
                                $('#users_table').empty()
                                submit()

                            }
                        })
                    }

                })
            )

            $('.table #users_table').append("</tr>")


        })


    }
    submit()

    $('.addUserBtn').click(function (event) {
        event.preventDefault()

        $.ajax({
            type: 'POST',
            url: '/mainPage/',
            data: JSON.stringify({
                userName: $('#exampleInputName').val(),
                age: $('#exampleInputAge').val(),
                password: $('#exampleInputPassword1').val(),
                userRoles: $('#exampleFormControlSelect2').val()
            }),
            headers: {'Content-type': 'application/json; charset=utf-8'},
            success: async function (response) {
                $('#users_table').empty()
                submit()
                $('#exampleInputName').val("")
                $('#exampleInputAge').val("")
                $('#exampleInputPassword1').val("")
                $('#exampleFormControlSelect2').val("")

            }
        })
    })

    $('.updateUserBtn').click(function (event){
        event.preventDefault()
        $.ajax({
            type: 'PUT',
            url: '/mainPage/',
            data: JSON.stringify({
                id: $('#userId').val(),
                userName: $('#userName').val(),
                age: $('#userAge').val(),
                password: $('#password').val(),
                userRoles: $('#roles').val()
            }),
            headers: {'Content-type': 'application/json; charset=utf-8'},
            success: async function (response) {
                $('#users_table').empty()
                submit()

            }
        })


    })




// $.ajax({
//     type: "POST",
//     url: "http://localhost:8080/mainPage/allUsers",
//     data: "IDS="+requests,
//     dataType: "json",
//     success: function(response){
//         const name = response;
//         //Important code starts here to populate table
//         const yourTableHTML = "";
//         jQuery.each(name, function(i,data) {
//             $("#friendlessness").append("<tr><td>" + data + "</td></tr>");
//         });
//     }
// });
})