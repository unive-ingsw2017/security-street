using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Services.Models;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    [Route("/autovelox", "POST")]
    [Route("/autovelox", "PUT")]
    public class UpdateOrCreateAutoveloxRequest : UpdateOrCreateRequest<AutoveloxDto>, IReturn<AutoveloxDto>
    {
    }
}
