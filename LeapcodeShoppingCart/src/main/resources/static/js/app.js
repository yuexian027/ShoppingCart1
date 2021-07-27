$(function() {
    $("a.confirmDeletion").click(function() {
        if (!confirm("Confirm deletion")) return false;
    });
    if ( $("#content").length){
            ClassEditor.create(document.querySelector("#content")).catch(error => {
                console.log(error);
            })
        }

        if ( $("#description").length){
                ClassEditor.create(document.querySelector("#description")).catch(error => {
                    console.log(error);
                })
        }

})
