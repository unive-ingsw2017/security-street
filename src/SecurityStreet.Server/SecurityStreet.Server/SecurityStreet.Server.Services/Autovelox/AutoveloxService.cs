using SecurityStreet.Server.Models.Dto;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    public class AutoveloxService : BaseCrudService<Server.Models.Entities.Autovelox, AutoveloxDto, ReadAutoveloxRequest, UpdateOrCreateAutoveloxRequest, DeleteAutoveloxRequest>
    {
    }
}