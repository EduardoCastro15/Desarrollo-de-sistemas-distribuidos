// Carlos Pineda G. 2012
#include <stdio.h>
#include <ctype.h>
#include <errno.h>
#define LONG_BUFFER (4*1024)
char buffer[LONG_BUFFER];
void main ()
{
  FILE *f = fopen("/dev/sdc","rb");
  int i;
  if (f == NULL)
  {
    fprintf(stderr,"Error al abrir el filesystem, errno=%i\n",errno);
    return;
  }
  for(;;)
  {
    fread(buffer,LONG_BUFFER,1,f);
    if (feof(f))
      break;
    for (i = 0; i < LONG_BUFFER; i++)
      if (isprint(buffer[i]))
        printf("%c",buffer[i]);
  }
}