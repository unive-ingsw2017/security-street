using SecurityStreet.Server.Services.Models;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    [Route("/autovelox/{Id}", "DELETE")]
    public class DeleteAutoveloxRequest : DeleteRequest
    {
    }
}
