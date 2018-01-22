using SecurityStreet.Server.Models.Dto;
using SecurityStreet.Server.Models.Entities;
using ServiceStack;

namespace SecurityStreet.Server.Services.Autovelox
{
    public class CrashesService : BaseService<Server.Models.Entities.Crash, CrashDto, ReadCrashRequest>
    {
    }
}