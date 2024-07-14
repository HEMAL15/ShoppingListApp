package com.example.shoppinglist
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
@Composable
fun shoppinglist()
{
//THE MAIN FUNCTION THATS UNDER THE POP UP ALERT DIALOG
    var sitems by remember { mutableStateOf(listOf<shoppingitems>()) }//creating a list of type of the data class name
    var showdialog by remember { mutableStateOf(true) }
    var itemName by remember { mutableStateOf("")}
    var itemQuan by remember { mutableStateOf("") }
    var i =0
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        Button(onClick = { showdialog=true },modifier= Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "ADD TO LIST")
        }
        LazyColumn(modifier= Modifier
            .fillMaxSize()
            .padding(16.dp))
        {
            items(sitems){
            item ->
                if(item.isEditing)
                {
                  shoppingitemeditor(item = item, onEditComplete ={
                      editedName, editedQuan ->
                      sitems=sitems.map{it.copy(isEditing = false)}
                      val editeditem=sitems.find{it.id==item.id}
                      editeditem?.let{
                          it.name=editedName
                          it.quantity=editedQuan
                      }
                  } )
                }
                else{
                    additems(item=item,onEditClick={
                        sitems=sitems.map{it.copy(isEditing = it.id==item.id )}},
                        onDeleteClick={
                            sitems=sitems-item


                    })
                }
            }

        }
    }
    if(showdialog)
    {
        //THIS IS THE POPUP THAT SHOWS UP WHILE ADDING AN ITEM
       AlertDialog(onDismissRequest = {showdialog=false},
           confirmButton = {  Row(modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),horizontalArrangement = Arrangement.SpaceBetween) {
               Button(onClick = {
                   if(itemName.isNotBlank() && itemQuan.isNotBlank() )
                   {
                       val newitem=shoppingitems(id = i+1, name=itemName, quantity = itemQuan.toInt())
                       sitems=sitems+newitem
                       showdialog=false
                       itemName=""
                   itemQuan=""}
               })
               {
                   Text(text = "ADD")
               }


               Button(onClick = { showdialog=false }) {
                   Text(text = "BACK")
               }
           } },
           title = {Text(text = "ADD ITEMS TO THE LIST")},
           text = {
               Column {
                   OutlinedTextField(value = itemName, onValueChange = {itemName=it},
                       singleLine = true,
                       modifier= Modifier
                           .fillMaxWidth()
                           .padding(8.dp))

                   OutlinedTextField(value = itemQuan, onValueChange = {itemQuan=it},
                       singleLine = true,
                       modifier= Modifier
                           .fillMaxWidth()
                           .padding(8.dp))

                   }
               }
       )

           }

    }
@Composable
//FOR EDITING OR DELETING AN ITEM AFTER ADDING IT
fun additems( item:shoppingitems,onEditClick:()->Unit, onDeleteClick:()->Unit)
{
Row(modifier= Modifier
    .padding(8.dp)
    .fillMaxWidth()
    .border(border = BorderStroke(2.dp, Color(0XFF018786)), shape = RoundedCornerShape(20)), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(text = item.name, modifier = Modifier.padding(8.dp))
    Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
    Row {
        IconButton(onClick = onEditClick) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
        }

    }
}
}
@Composable
//THIS IS THE UI THAT SHOWS UP IF U CLICK ON THE EDIT OPTION
fun shoppingitemeditor(item: shoppingitems, onEditComplete: (String,Int)->Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedquan by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly
    )
    {
        Column {
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
            BasicTextField(
                value = editedquan,
                onValueChange = { editedquan = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )
        }
        Button(onClick = { isEditing=false
        onEditComplete(editedName,editedquan.toIntOrNull()?:1)})
        {
            Text(text = "save")
        }
    }
}
data class shoppingitems(val id:Int, var name:String, var quantity: Int, var isEditing: Boolean=false )
@Composable
@Preview(showBackground = true)
fun shoppinglistPreview()
{
    shoppinglist()
}