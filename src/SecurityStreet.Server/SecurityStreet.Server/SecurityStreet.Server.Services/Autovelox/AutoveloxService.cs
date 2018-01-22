using SecurityStreet.Server.Models.Dto;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    public class AutoveloxService : BaseService<Server.Models.Entities.Autovelox, AutoveloxDto, ReadAutoveloxRequest>
    {
    }
}