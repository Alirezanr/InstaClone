package dan.nr.sample.util

object InputValidator
{
    fun validateInputs(name:String,password:String): Boolean
    {
        return (name.isNotEmpty() && password.isNotEmpty())
    }
}